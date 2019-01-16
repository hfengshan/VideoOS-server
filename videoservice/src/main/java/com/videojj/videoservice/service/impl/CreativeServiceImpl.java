package com.videojj.videoservice.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.Page;
import com.videojj.videocommon.constant.Constants;
import com.videojj.videocommon.exception.ServiceException;
import com.videojj.videocommon.util.DateUtil;
import com.videojj.videocommon.util.VideoUtil;
import com.videojj.videoservice.annotation.OperationLogAnnotationService;
import com.videojj.videoservice.config.CommonConfig;
import com.videojj.videoservice.dao.TbCreativeFileMapper;
import com.videojj.videoservice.dao.TbCreativeMapper;
import com.videojj.videoservice.dto.*;
import com.videojj.videoservice.entity.TbCreative;
import com.videojj.videoservice.entity.TbCreativeCriteria;
import com.videojj.videoservice.entity.TbCreativeFile;
import com.videojj.videoservice.entity.TbCreativeFileCriteria;
import com.videojj.videoservice.enums.CreativeStatusEnum;
import com.videojj.videoservice.enums.IsDeletedEnum;
import com.videojj.videoservice.enums.OperationLogTypeEnum;
import com.videojj.videoservice.fileserver.CommonFileServer;
import com.videojj.videoservice.service.CreativeService;
import com.videojj.videoservice.service.OperationLogService;
import com.videojj.videoservice.util.ImageUtil;
import com.videojj.videoservice.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;



/**
 * @Author @videopls.com
 * Created by  on 2018/8/8 下午4:28.
 * @Description:
 */
@Service
public class CreativeServiceImpl implements CreativeService{

    private static Logger log = LoggerFactory.getLogger("CreativeServiceImpl");

    @Resource
    private TbCreativeMapper tbCreativeMapper;

    @Resource
    private TbCreativeFileMapper tbCreativeFileMapper;

    @Resource
    private CommonFileServer ossUtil;

    @Resource
    private CommonConfig commonConfig;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private OperationLogService operationLogService;

    private final String key = "hotspotArray";

    @Override
    public AddUploadCreativeResponseDTO addUpload(HttpServletRequest request,String username, MultipartFile multipartTemplateFile,Integer creativeId) {

        Map<String,Object> uploadResult = uploadFile(request,multipartTemplateFile);

        TbCreativeFile param = new TbCreativeFile();

        param.setCreator(username);

        param.setFileName(uploadResult.get("fileName").toString());

        String url = commonConfig.getFileDomainName().concat(commonConfig.getPreKey()).concat(uploadResult.get("fileName").toString());

        param.setIsDeleted(IsDeletedEnum.NO.getValue());

        param.setModifier(username);

        param.setFileUrl(url);

        if(null != creativeId) {

            param.setCreativeId(creativeId);
        }
        tbCreativeFileMapper.insertSelective(param);

        AddUploadCreativeResponseDTO res = new AddUploadCreativeResponseDTO();

        res.setFileUrl(url);
        if(null != uploadResult.get("height")) {

            res.setHeight(Integer.parseInt(uploadResult.get("height").toString()));
        }

        if(null != uploadResult.get("width")) {

            res.setWidth(Integer.parseInt(uploadResult.get("width").toString()));
        }

        if(null != uploadResult.get("duration")) {

            res.setDuration((int)uploadResult.get("duration"));
        }

        res.setResMsg(Constants.COMMONSUCCESSMSG);

        res.setResCode(Constants.SUCESSCODE);

        res.setCreativeFileId(param.getId());

        return res;
    }

    private Map<String,Object> uploadFile(HttpServletRequest request,MultipartFile multipartTemplateFile) {

        Map<String,Object> result = new HashMap<>();

        String suffix = null;

        String prifixFileName = null;
        try {

            String fileName = multipartTemplateFile.getOriginalFilename();

            if(fileName.split("\\.").length==0){

                throw new ServiceException("文件没有后缀名");
            }

            List<String> namePartList = Arrays.asList(fileName.split("\\.")).stream().collect(Collectors.toList());

            suffix = namePartList.get(namePartList.size()-1);

            InputStream in = multipartTemplateFile.getInputStream();

            if(fileName.endsWith(".gif")||fileName.endsWith(".jpg")||fileName.endsWith(".png")) {
                // 此处是文件流的处理
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = in.read(buffer)) > -1) {
                    baos.write(buffer, 0, len);
                }
                baos.flush();

                InputStream streamCopy1 = new ByteArrayInputStream(baos.toByteArray());

                InputStream streamCopy2 = new ByteArrayInputStream(baos.toByteArray());

                in = new ByteArrayInputStream(baos.toByteArray());

                Integer height = ImageUtil.getImgHeight(streamCopy1);

                Integer width = ImageUtil.getImgWidth(streamCopy2);

                result.put("height",height);

                result.put("width",width);

            }
            else if(fileName.endsWith(".mp4")){
                result.put("duration",VideoUtil.getVideoFileLength(request,multipartTemplateFile));
            }
            prifixFileName = UUID.randomUUID().toString();

            ossUtil.upload(commonConfig.getPreKey()+ prifixFileName+"."+suffix,in);

            result.put("fileName",prifixFileName+"."+suffix);

            return result;

        } catch (IOException ioe) {

            log.error("CreativeServiceImpl.uploadFile ==> 获取文件失败!!",ioe);

            throw new ServiceException("获取文件失败");
        }catch (ServiceException se){

            throw se;
        }catch (Exception e) {

            log.error("CreativeServiceImpl.uploadFile ==> upload file error",e);

            throw new ServiceException("上传文件失败");
        }
    }

    @Override
    public UpdateUploadCreativeResponseDTO updateUpload(HttpServletRequest request,String username, Integer creativeFileId, Integer creativeId, MultipartFile multipartTemplateFile) {

        // 如果creativeFileId为空的话，原来不存在，新增的文件，按新增流程，如果不为空的情况按修改流程。。
        if(creativeFileId == null){

            AddUploadCreativeResponseDTO res = addUpload(request,username,multipartTemplateFile,creativeId);

            UpdateUploadCreativeResponseDTO upres = new UpdateUploadCreativeResponseDTO();

            upres.setCreativeFileId(res.getCreativeFileId());

            upres.setFileUrl(res.getFileUrl());

            upres.setResCode(Constants.SUCESSCODE);

            upres.setResMsg(Constants.COMMONSUCCESSMSG);

            upres.setHeight(res.getHeight());

            upres.setWidth(res.getWidth());

            upres.setDuration(res.getDuration());

            return upres;

        }else{
            // 如果是修改的流程，那么就要先修改上传新文件然后更新文件表，然后启用一个线程，去删除旧的文件
            Map<String,Object> uploadResult = uploadFile(request,multipartTemplateFile);

            TbCreativeFile param = new TbCreativeFile();

            param.setId(creativeFileId);
            // 主要是新增和修改素材文件，都需要返回图片的大小
            param.setFileName(uploadResult.get("fileName").toString());

            String url = commonConfig.getFileDomainName().concat(commonConfig.getPreKey()).concat(uploadResult.get("fileName").toString());

            param.setFileUrl(url);

            tbCreativeFileMapper.updateByPrimaryKeySelective(param);

            UpdateUploadCreativeResponseDTO upres = new UpdateUploadCreativeResponseDTO();

            if(null != uploadResult.get("height")) {

                upres.setHeight(Integer.parseInt(uploadResult.get("height").toString()));
            }

            if(null != uploadResult.get("width")) {

                upres.setWidth(Integer.parseInt(uploadResult.get("width").toString()));
            }

            if(null != uploadResult.get("duration")) {

                upres.setDuration((int)uploadResult.get("duration"));
            }

            upres.setCreativeFileId(creativeFileId);

            upres.setFileUrl(url);

            upres.setResCode(Constants.SUCESSCODE);

            upres.setResMsg(Constants.COMMONSUCCESSMSG);

            return upres;
        }

    }

    @OperationLogAnnotationService(descArgPositions = {0},fieldNames = {"creativeName"},type = OperationLogTypeEnum._62)
    @Override
    public void updateInfo(UpdateCreativeRequestDTO request, String username) {

        TbCreativeCriteria qryParam = new TbCreativeCriteria();

        TbCreativeCriteria.Criteria creativeCri = qryParam.createCriteria();

        creativeCri.andMaterialNameEqualTo(request.getCreativeName());

        List<TbCreative> tbCreatives = tbCreativeMapper.selectByParam(qryParam);

        if(!CollectionUtils.isEmpty(tbCreatives)&&tbCreatives.get(0).getId().intValue()!=request.getCreativeId()){

            throw new ServiceException("该素材名称已经存在，请换一个名称");
        }

        TbCreative upParam = new TbCreative();

        upParam.setId(request.getCreativeId());

        upParam.setTemplateId(request.getInteractionTemplateId());

        upParam.setModifier(username);

        upParam.setMaterialName(request.getCreativeName());

        upParam.setInteractionId(request.getInteractionTypeId());

        upParam.setMaterialContent(request.getCreativeContent());

        upParam.setInteractionName(request.getInteractionTypeName());

        upParam.setTemplateName(request.getInteractionTemplateName());

        String value = JsonUtil.getValueByKeyFromJson(request.getCreativeContent(),key);

        Integer hotspot = 1;

        if(!JsonUtil.NOTEXIST.equals(value)) {

            JSONArray jsonArr = JSONArray.parseArray(value);

            hotspot = jsonArr.size();

        }

        upParam.setHotSpotNum(hotspot);

        tbCreativeMapper.updateByPrimaryKeySelective(upParam);

    }

    @Override
    public void deleteCreativeFileByUrl(String fileUrl, String username) {

        List<String> pathPart = Arrays.asList(fileUrl.split("/"));

        String fileName = pathPart.get(pathPart.size()-1);

        TbCreativeFileCriteria condition = new TbCreativeFileCriteria();

        TbCreativeFileCriteria.Criteria fileCri = condition.createCriteria();

        fileCri.andFileUrlEqualTo(fileUrl);

        TbCreativeFile param = new TbCreativeFile();

        param.setIsDeleted(IsDeletedEnum.YES.getValue());

        transactionTemplate.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean doInTransaction(TransactionStatus tranStatus) {
                try{
                    tbCreativeFileMapper.updateByCriteriaSelective(param,condition);

                    ossUtil.delete(commonConfig.getPreKey()+fileName);

                }catch(Exception e){

                    log.error("CreativeServiceImpl.deleteCreativeFileByUrl ==> delete error",e);

                    throw new ServiceException("删除时报错!!!");
                }
                return Boolean.TRUE;
            }
        });


    }

    @Override
    public CreativePageInfoResponseDTO queryPageInfoByParam(Integer interactionTypeId) {

        CreativePageInfoResponseDTO creativePageInfoResponseDTO = new CreativePageInfoResponseDTO();

        Page<TbCreative> page =  tbCreativeMapper.selectPage(interactionTypeId);

        creativePageInfoResponseDTO.setResCode(Constants.SUCESSCODE);

        creativePageInfoResponseDTO.setResMsg(Constants.COMMONSUCCESSMSG);

        if(page.getTotal() ==0 ){
            creativePageInfoResponseDTO.setTotalPage(0);

            creativePageInfoResponseDTO.setTotalRecord(0L);

            return creativePageInfoResponseDTO;
        }

        creativePageInfoResponseDTO.setTotalPage(page.getPages());

        creativePageInfoResponseDTO.setTotalRecord(page.getTotal());

        List<CreativePageInfoResponseDTO.CreativeInfo> roleInfoList = convertCreativeParam(page);

        creativePageInfoResponseDTO.setCreativeInfoList(roleInfoList);

        return creativePageInfoResponseDTO;

    }

    @Override
    public CreativeDetailInfoResponseDTO queryDetailById(Integer creativeId) {

        TbCreative tbCreativeExt = tbCreativeMapper.selectJoinInfoByPrimaryKey(creativeId);

        CreativeDetailInfoResponseDTO res = new CreativeDetailInfoResponseDTO();

        res.setResMsg(Constants.COMMONSUCCESSMSG);

        res.setResCode(Constants.SUCESSCODE);

        if(null == tbCreativeExt){

            return res;
        }
        res.setTemplateName(tbCreativeExt.getTemplateName());

        res.setTemplateId(tbCreativeExt.getTemplateId());

        res.setCreativeName(tbCreativeExt.getMaterialName());

        res.setCreativeContent(tbCreativeExt.getMaterialContent());

        res.setInteractionId(tbCreativeExt.getInteractionId());

        res.setInteractionName(tbCreativeExt.getInteractionName());

        res.setCreativeId(creativeId);

        res.setHotSpotNum(tbCreativeExt.getHotSpotNum());

        return res;
    }

    @Override
    public CreativePageInfoResponseDTO queryAll(Integer interactionType) {

        TbCreativeCriteria param = new TbCreativeCriteria();

        param.setOrderByClause("gmt_created desc");

        TbCreativeCriteria.Criteria creativeCri = param.createCriteria();

        creativeCri.andIsDeletedEqualTo("N");

        if(null != interactionType){

            creativeCri.andInteractionIdEqualTo(interactionType);
        }

        List<TbCreative> creativeList = tbCreativeMapper.selectByParam(param);

        List<CreativePageInfoResponseDTO.CreativeInfo> creativeInfoList = convertCreativeParam(creativeList);

        CreativePageInfoResponseDTO res = new CreativePageInfoResponseDTO();

        res.setCreativeInfoList(creativeInfoList);

        res.setResCode(Constants.SUCESSCODE);

        res.setResMsg(Constants.COMMONSUCCESSMSG);

        return res;
    }

    private List<CreativePageInfoResponseDTO.CreativeInfo> convertCreativeParam(List<TbCreative> tbCreativeList) {

        if(CollectionUtils.isEmpty(tbCreativeList)){
            return null;
        }

        List<CreativePageInfoResponseDTO.CreativeInfo> creativeInfoList = new ArrayList<>();

        for(TbCreative tbCreative:tbCreativeList){

            CreativePageInfoResponseDTO.CreativeInfo creativeInfo = new CreativePageInfoResponseDTO.CreativeInfo();

            creativeInfo.setInteractionId(tbCreative.getInteractionId());

            creativeInfo.setCreateDate(DateUtil.toFormatDateString(tbCreative.getGmtCreated(),DateUtil.DATE_FORMAT));

            creativeInfo.setCreativeName(tbCreative.getMaterialName());

            creativeInfo.setCreativeStatus(tbCreative.getStatus());

            creativeInfo.setInteractionName(tbCreative.getInteractionName());

            creativeInfo.setTemplateId(tbCreative.getTemplateId());

            creativeInfo.setTemplateName(tbCreative.getTemplateName());

            creativeInfo.setCreativeId(tbCreative.getId());

            creativeInfo.setHotSpotNum(tbCreative.getHotSpotNum());

            creativeInfoList.add(creativeInfo);
        }

        return creativeInfoList;
    }

    @OperationLogAnnotationService(descArgPositions = {0},fieldNames = {"creativeName"},type = OperationLogTypeEnum._61)
    @Override
    public void addCreative(AddCreativeRequestDTO request) {

        TbCreativeCriteria qryParam = new TbCreativeCriteria();
        TbCreativeCriteria.Criteria creativeCri = qryParam.createCriteria();
        creativeCri.andMaterialNameEqualTo(request.getCreativeName());
        List<TbCreative> tbCreatives = tbCreativeMapper.selectByParam(qryParam);

        if(!CollectionUtils.isEmpty(tbCreatives)){

            throw new ServiceException("该素材名称已经存在，请换一个名称");
        }
        TbCreative param = new TbCreative();

        param.setInteractionId(request.getInteractionTypeId());
        param.setIsDeleted(IsDeletedEnum.NO.getValue());
        param.setCreator(request.getUsername());

        param.setMaterialContent(request.getCreativeContent());
        param.setMaterialName(request.getCreativeName());
        param.setModifier(request.getUsername());
        param.setStatus(CreativeStatusEnum.NO_USE.getValue());
        param.setTemplateId(request.getInteractionTemplateId());

        param.setInteractionName(request.getInteractionTypeName());
        param.setTemplateName(request.getInteractionTemplateName());

        String value = JsonUtil.getValueByKeyFromJson(request.getCreativeContent(),key);

        /**热点数，最少是1*/
        Integer hotspot = 1;

        if(!JsonUtil.NOTEXIST.equals(value)) {

            JSONArray jsonArr = JSONArray.parseArray(value);

            hotspot = jsonArr.size();

        }
        param.setHotSpotNum(hotspot);

        transactionTemplate.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean doInTransaction(TransactionStatus status) {
                try{
                    tbCreativeMapper.insertSelective(param);

                    if(!CollectionUtils.isEmpty(request.getCreativeIdList())) {

                        TbCreativeFileCriteria condition = new TbCreativeFileCriteria();

                        TbCreativeFileCriteria.Criteria fileCriteria = condition.createCriteria();

                        fileCriteria.andIdIn(request.getCreativeIdList());

                        TbCreativeFile fileParam = new TbCreativeFile();

                        fileParam.setCreativeId(param.getId());

                        tbCreativeFileMapper.updateByCriteriaSelective(fileParam, condition);
                    }
                }catch(Exception e){

                    log.error("CreativeServiceImpl.addCreative save error",e);

                    throw new ServiceException("报错数据库报错!!!");
                }
                return Boolean.TRUE;
            }
        });
    }

    @Override
    public void deleteCreativeByCreativeId(Integer creativeId, String username) {

        TbCreative param = new TbCreative();

        param.setIsDeleted(IsDeletedEnum.YES.getValue());

        param.setModifier(username);

        TbCreativeCriteria creativeCriteria = new TbCreativeCriteria();

        TbCreativeCriteria.Criteria criteria = creativeCriteria.createCriteria();

        criteria.andIdEqualTo(creativeId).andIsDeletedEqualTo(IsDeletedEnum.NO.getValue());

        transactionTemplate.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean doInTransaction(TransactionStatus status) {
                try{

                    tbCreativeMapper.updateByParamSelective(param, creativeCriteria);

                    TbCreativeFileCriteria condition = new TbCreativeFileCriteria();

                    TbCreativeFileCriteria.Criteria fileCriteria = condition.createCriteria();

                    fileCriteria.andCreativeIdEqualTo(creativeId);

                    fileCriteria.andIsDeletedEqualTo(IsDeletedEnum.NO.getValue());

                    List<TbCreativeFile> tbFileList =  tbCreativeFileMapper.selectByCriteria(condition);

                    if(!CollectionUtils.isEmpty(tbFileList)) {

                        String fileName = tbFileList.get(0).getFileName();

                        TbCreativeFile fileParam = new TbCreativeFile();

                        fileParam.setIsDeleted(IsDeletedEnum.YES.getValue());

                        tbCreativeFileMapper.updateByCriteriaSelective(fileParam, condition);

                        ossUtil.delete(commonConfig.getPreKey()+fileName);
                    }
                }catch(Exception e){

                    log.error("CreativeServiceImpl.deleteCreativeByCreativeId ==> save error",e);

                    throw new ServiceException("保存数据报错，检查参数!!!");
                }
                return Boolean.TRUE;
            }
        });

        // 记录操作日志
        operationLogService.writeOperationLog_63(tbCreativeMapper.selectByPrimaryKey(creativeId).getMaterialName());
    }


}
