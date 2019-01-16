package com.videojj.videoservice.service.impl;

import com.videojj.videocommon.constant.Constants;
import com.videojj.videocommon.exception.ServiceException;
import com.videojj.videocommon.util.DateUtil;
import com.videojj.videoservice.bo.QueryTemplateParamBo;
import com.videojj.videoservice.bo.TemplateSimpleInfoBo;

import com.videojj.videoservice.dao.RedisSessionDao;
import com.videojj.videoservice.dao.TbCreativeMapper;
import com.videojj.videoservice.dto.*;
import com.videojj.videoservice.entity.*;
import com.videojj.videoservice.enums.IsDeletedEnum;

import com.videojj.videoservice.service.InteractionZipFileService;
import com.videojj.videoservice.util.FileUtil;
import com.videojj.videoservice.util.ZipUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import javax.annotation.Resource;
import java.io.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/6 下午3:05.
 * @Description:
 */
@Service
public class InteractionZipFileServiceImpl extends AbstractTemplateService implements InteractionZipFileService {

    private static Logger log = LoggerFactory.getLogger("InteractionZipFileServiceImpl");

    @Resource
    private RedisSessionDao redisSessionDao;

    @Resource
    private TbCreativeMapper tbCreativeMapper;

    @Override
    public TemplatePageInfoResponseDTO queryPageInfoByParam(TemplatePageInfoRequestDTO templatePageInfoRequestDTO) {

        int limitstart=(templatePageInfoRequestDTO.getCurrentPage()-1) * templatePageInfoRequestDTO.getPageSize();

        QueryTemplateParamBo paramBo = new QueryTemplateParamBo(limitstart, templatePageInfoRequestDTO.getPageSize());

        TbTemplateCriteria countParam = new TbTemplateCriteria();

        TbTemplateCriteria.Criteria tbTcri = countParam.createCriteria();

        if(null != templatePageInfoRequestDTO.getInteractionTypeId()){

            paramBo.setInteractionTypeId(templatePageInfoRequestDTO.getInteractionTypeId());

            tbTcri.andInteractionIdEqualTo(templatePageInfoRequestDTO.getInteractionTypeId());
        }

        List<TbTemplate> tbTemplateList =  tbTemplateMapper.selectByParamWithPage(paramBo);

        tbTcri.andIsDeletedEqualTo(IsDeletedEnum.NO.getValue());

        int allcount = tbTemplateMapper.countByParam(countParam);

        TemplatePageInfoResponseDTO templatePageInfoResponseDTO = new TemplatePageInfoResponseDTO();

        templatePageInfoResponseDTO.setResCode(Constants.SUCESSCODE);

        templatePageInfoResponseDTO.setResMsg(Constants.COMMONSUCCESSMSG);

        if (allcount == 0){

            templatePageInfoResponseDTO.setTotalPage(0);

            templatePageInfoResponseDTO.setTotalRecord(0);

            return templatePageInfoResponseDTO;
        }

        if(CollectionUtils.isNotEmpty(tbTemplateList)) {
            // 此处要进行创建一个方法
            List<TemplatePageInfoResponseDTO.TemplateInfo> templateInfoList = batchTransform(tbTemplateList);

            templatePageInfoResponseDTO.setTemplateInfoList(templateInfoList);
        }

        if(allcount % templatePageInfoRequestDTO.getPageSize()==0){
            templatePageInfoResponseDTO.setTotalPage(allcount / templatePageInfoRequestDTO.getPageSize());
        }else{
            templatePageInfoResponseDTO.setTotalPage((allcount / templatePageInfoRequestDTO.getPageSize()) + 1);
        }
        templatePageInfoResponseDTO.setTotalRecord(allcount);

        return templatePageInfoResponseDTO;
    }

    @Override
    public void deleteInteractionByTypeName(Integer interactionTemplateId,String username){

        String downloadFilePath = commonConfig.getFilePath()+ UUID.randomUUID()+Constants.SLASH;

        File downLoadPathFile = new File(downloadFilePath);

        downLoadPathFile.mkdir();

        String downLoadFileName = null;

        String newVersion = null;

        String compressPath = null;

        // 1、查询文件表最新版本号,然后查询模版表，查到要删除文件的文件名称，以及相应的路径
        TbTemplateZipFile tbTemplateZipFile = queryNewestZipFile();

        TbTemplate tbTemplate = tbTemplateMapper.selectByPrimaryKey(interactionTemplateId);
        try {
            // 2、oss上下载文件
            downloadFile(tbTemplateZipFile.getFileName(), downloadFilePath + downLoadFileName);
        }catch (Exception e){

            log.error("InteractionZipFileServiceImpl.deleteInteractionByTypeName ==> download file error!!,downloadFilePath is {}",downloadFilePath,e);

            throw new ServiceException("从oss下载文件时报错!!");
        }
        newVersion = getNewVersion(tbTemplateZipFile.getFileVersion());

        compressPath = downloadFilePath + tbTemplateZipFile.getFileVersion() +TEMPLATESTR+Constants.SLASH;

        try {
            // 3、解压文件到本地
            ZipUtil.unzip(new File(downloadFilePath + downLoadFileName), downloadFilePath);

            // 4、删除第一步查询出来的文件
            deleteFile(tbTemplate.getTemplateFileSourceName(), compressPath);

            File oldPath = new File(downloadFilePath+tbTemplateZipFile.getFileVersion()+TEMPLATESTR);

            oldPath.renameTo(new File(downloadFilePath+newVersion+TEMPLATESTR));
            // 5、打包文件夹
            ZipUtil.toZip(downloadFilePath+newVersion+TEMPLATESTR,
                    new FileOutputStream(new File(downloadFilePath + newVersion + FILENAMESUFFIX)), true);
        }catch (Exception e){

            log.error("InteractionZipFileServiceImpl.deleteInteractionByTypeName ==> handle file error!!,downloadFilePath is {}",downloadFilePath,e);

            throw new ServiceException("本地处理文件报错!!");
        }
        InputStream up = null;
        try {
            // 6、上传文件
            up = new FileInputStream(new File(downloadFilePath + newVersion + FILENAMESUFFIX));

            commonFileServer.upload(commonConfig.getPreKey() + newVersion + FILENAMESUFFIX, up);
        }catch (Exception e){

            log.error("InteractionZipFileServiceImpl.deleteInteractionByTypeName ==> upload file error!!,downloadFilePath is {}",downloadFilePath,e);

            throw new ServiceException("上传文件时报错!!!");
        }finally {

            try {
                up.close();
            } catch (IOException e) {
            }
        }
        final String fnewVersion = newVersion;
        // 7、开启事务插入一条数据，在file表中
        transanctionTemplate.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean doInTransaction(TransactionStatus status) {

                try{

                    TbTemplateZipFile tbTemplateZipFile = new TbTemplateZipFile();

                    tbTemplateZipFile.setFileVersion(fnewVersion);

                    tbTemplateZipFile.setOssurl(commonConfig.getFileDomainName().concat(commonConfig.getPreKey()).concat(fnewVersion).concat(FILENAMESUFFIX));

                    tbTemplateZipFile.setFileName(fnewVersion+FILENAMESUFFIX);

                    tbTemplateZipFile.setIsDeleted(IsDeletedEnum.NO.getValue());

                    tbTemplateZipFile.setModifier(username);

                    tbTemplateZipFile.setCreator(username);

                    tbTemplateZipFile.setStatus((byte) 3);

                    tbTemplateZipFileMapper.insertSelective(tbTemplateZipFile);

                    // 8、更新模版表，数据为逻辑删除 事务结束

                    TbTemplate tbTemplate = new TbTemplate();

                    tbTemplate.setIsDeleted(IsDeletedEnum.YES.getValue());

                    tbTemplate.setModifier(username);

                    TbTemplateCriteria tbTemplateCriteria = new TbTemplateCriteria();

                    TbTemplateCriteria.Criteria tbCriteria = tbTemplateCriteria.createCriteria();

                    tbCriteria.andIdEqualTo(interactionTemplateId);

                    tbTemplateMapper.updateByParamSelective(tbTemplate,tbTemplateCriteria);

                    tbTemplateFileMapper.deleteByTemplateId(interactionTemplateId);

                    /**
                     * 8.2、逻辑删除对应的素材
                     * 2018-11-30 by zhangzhewe
                     */
                    tbCreativeMapper.logicallyDeleteByTemplateId(interactionTemplateId);

                }catch(Exception e){

                    log.error("InteractionZipFileServiceImpl.deleteInteractionByTypeName ==> delete templateinfo error!!",e);

                    throw new ServiceException("数据库更新数据时报错!! ");
                }
                return Boolean.TRUE;
            }
        });
        // 9、新启动线程，删除本地的那个文件夹
        DeleteFileThread deleteFileThread = new DeleteFileThread(downloadFilePath);

        new Thread(deleteFileThread).start();

        // 10、记录操作日志
        operationLogService.writeOperationLog_23(tbTemplate.getTemplateName());
    }

    @Override
    public TemplateDetailInfoResponseDTO queryTemplateById(Integer templateId) {

        TemplateDetailInfoResponseDTO res = new TemplateDetailInfoResponseDTO();

        TemplateSimpleInfoBo templateSimpleInfo = tbTemplateMapper.selectByTemplateId(templateId);

        if(null == templateSimpleInfo){

            res.setResCode(Constants.SUCESSCODE);

            res.setResMsg(Constants.COMMONSUCCESSMSG);

            return res;
        }
        res.setInteractionTypeName(templateSimpleInfo.getInteractionName());

        res.setInteractionTypeId(templateSimpleInfo.getInteractionId());

        res.setTemplateId(templateSimpleInfo.getTemplateId());

        res.setTemplateName(templateSimpleInfo.getTemplateName());

        res.setTemplateFileSourceName(templateSimpleInfo.getTemplateFileSourceName());

        return res;
    }

    @Override
    public File getTemplateFile(Integer templateId,String token) {

        if(StringUtils.isNotEmpty(token)){

            Session session = redisSessionDao.doReadSession(token);

            if(session == null){

                throw new ServiceException("传递的token无效");
            }
        }

        String downloadFilePath = commonConfig.getFilePath()+ UUID.randomUUID()+Constants.SLASH;

        File downLoadPathFile = new File(downloadFilePath);

        downLoadPathFile.mkdir();

        String downLoadFileName = null;

        String compressPath = null;

        // 1、，查到要下载的文件的文件名称，以及相应的路径
        TbTemplateZipFile tbTemplateZipFile = queryNewestZipFile();

        TbTemplate tbTemplate = tbTemplateMapper.selectByPrimaryKey(templateId);
        try {
            // 2、oss上下载文件
            downloadFile(tbTemplateZipFile.getFileName(), downloadFilePath + downLoadFileName);
        }catch (Exception e){

            log.error("InteractionZipFileServiceImpl.getTemplateFile ==> download file error!!,downloadFilePath is {}",downloadFilePath,e);

            throw new ServiceException("从oss上下载文件报错!!");
        }
        compressPath = downloadFilePath + tbTemplateZipFile.getFileVersion() +TEMPLATESTR+Constants.SLASH;

        try {
            // 3、解压文件到本地
            ZipUtil.unzip(new File(downloadFilePath + downLoadFileName), downloadFilePath);

            // 4、返回文件
            File file = new File(compressPath+tbTemplate.getTemplateFileSourceName());

            return file;
        }catch(Exception e){

            log.error("InteractionZipFileServiceImpl.getTemplateFile ==> download file error!!,downloadFilePath is {}",downloadFilePath,e);

            throw new ServiceException("解压文件报错");
        }
    }

    @Override
    public TemplatePageInfoResponseDTO queryAllByParam(TemplatePageInfoRequestDTO templatePageInfoRequestDTO) {

        TemplatePageInfoResponseDTO templatePageInfoResponseDTO = new TemplatePageInfoResponseDTO();

        QueryTemplateParamBo paramBo = new QueryTemplateParamBo(null,null);

        if(null != templatePageInfoRequestDTO.getInteractionTypeId()){

            paramBo.setInteractionTypeId(templatePageInfoRequestDTO.getInteractionTypeId());
        }


        List<TbTemplate> tbTemplateList =  tbTemplateMapper.selectByParamWithPage(paramBo);

        if(CollectionUtils.isNotEmpty(tbTemplateList)) {
            // 此处要进行创建一个方法
            List<TemplatePageInfoResponseDTO.TemplateInfo> templateInfoList = batchTransform(tbTemplateList);

            templatePageInfoResponseDTO.setTemplateInfoList(templateInfoList);
        }

        templatePageInfoResponseDTO.setResCode(Constants.SUCESSCODE);

        templatePageInfoResponseDTO.setResMsg(Constants.COMMONSUCCESSMSG);

        return templatePageInfoResponseDTO;
    }

    private void deleteFile(String templateFileName, String compressPath) {

        File file = new File(compressPath+templateFileName);

        if(file.exists()) {

            Boolean deleteResult = FileUtil.deleteFile(compressPath + templateFileName);

            if (!deleteResult) {

                throw new ServiceException("删除文件失败。。。");
            }
        }else{

            return;
        }
    }

    private List<TemplatePageInfoResponseDTO.TemplateInfo> batchTransform(List<TbTemplate> tbTemplateList) {

        List<TemplatePageInfoResponseDTO.TemplateInfo> templateInfoList = new ArrayList<>();

        for(TbTemplate tbTemplate:tbTemplateList){

            TemplatePageInfoResponseDTO.TemplateInfo tbTemplateInfo = new TemplatePageInfoResponseDTO.TemplateInfo();

            tbTemplateInfo.setTemplateName(tbTemplate.getTemplateName());
            tbTemplateInfo.setCreateDate(DateUtil.toShortDateString(tbTemplate.getGmtCreated()));
            tbTemplateInfo.setInteractionTypeId(tbTemplate.getInteractionId());
            tbTemplateInfo.setInteractionTypeName(tbTemplate.getInteractionTypeName());
            tbTemplateInfo.setTemplateId(tbTemplate.getId());

            templateInfoList.add(tbTemplateInfo);
        }
        return templateInfoList;

    }

}
