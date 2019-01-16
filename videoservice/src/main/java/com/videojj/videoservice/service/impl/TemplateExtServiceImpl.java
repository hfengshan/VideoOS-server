package com.videojj.videoservice.service.impl;

import com.videojj.videocommon.constant.Constants;
import com.videojj.videocommon.exception.ServiceException;
import com.videojj.videoservice.annotation.OperationLogAnnotationService;
import com.videojj.videoservice.bo.TemplateAddBo;
import com.videojj.videoservice.cache.FileVersionCache;
import com.videojj.videoservice.dao.TbCreativeMapper;
import com.videojj.videoservice.entity.*;
import com.videojj.videoservice.enums.IsDeletedEnum;
import com.videojj.videoservice.enums.OperationLogTypeEnum;
import com.videojj.videoservice.service.TemplateExtService;
import com.videojj.videoservice.util.FileUtil;
import com.videojj.videoservice.util.StreamWrapper;
import com.videojj.videoservice.util.ZipUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @Author @videopls.com
 * Created by  on 2018/10/26 上午11:31.
 * @Description:
 */
@Service
public class TemplateExtServiceImpl extends AbstractTemplateService implements TemplateExtService {

    private static Logger log = LoggerFactory.getLogger("TemplateExtServiceImpl");

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private TbCreativeMapper tbCreativeMapper;

    @Resource
    private FileVersionCache fileVersionCache;

    /**上传文件*/
    @Override
    public TemplateAddBo uploadFile(InputStream uploadStream,String zipFileName) {

        if(uploadStream == null){
            log.error("TemplateExtServiceImpl.uploadFile ==> no file upload...");
            throw new ServiceException("没有收到上传的文件，请检查参数哦！");
        }

        StreamWrapper streamWrapper = new StreamWrapper(uploadStream);

        TemplateAddBo templateAddBo = new TemplateAddBo();

        String downloadFilePath = commonConfig.getFilePath() + UUID.randomUUID() + Constants.SLASH;

        Boolean checkRes = checkParam(null, zipFileName, streamWrapper, templateAddBo);

        if (!checkRes) {

            return templateAddBo;
        }

        // 4、查询最新模版的版本号
        TbTemplateZipFile tbTemplateZipFile = queryNewestZipFile();

        String compressPath = null;

        String newVersion = null;

        if (null != tbTemplateZipFile) {

            String downLoadFileName = tbTemplateZipFile.getFileVersion() + TEMPLATESTR + Constants.ZIPSUFFIX;

            File downLoadPathFile = new File(downloadFilePath);
            downLoadPathFile.mkdir();
            // 5、下载模版压缩包
            downloadFile(tbTemplateZipFile.getFileName(), downloadFilePath + downLoadFileName);
            // 6、 产生下一个版本号
            newVersion = getNewVersion(tbTemplateZipFile.getFileVersion());

            templateAddBo.setNewVersion(newVersion);
            //解压的路径
            compressPath = downloadFilePath + tbTemplateZipFile.getFileVersion() + TEMPLATESTR + Constants.SLASH;

            // 7、解压下载的压缩包
            depressZipFile(downloadFilePath, downLoadFileName);

            saveLocalFile(streamWrapper, zipFileName, compressPath);

            File oldPath = new File(downloadFilePath + tbTemplateZipFile.getFileVersion() + TEMPLATESTR);
            // 8、更新成最新的版本号
            oldPath.renameTo(new File(downloadFilePath + newVersion + TEMPLATESTR));

        } else {

            newVersion = getNewVersion(null);

            templateAddBo.setNewVersion(newVersion);

            compressPath = downloadFilePath + INITFILEVERSIONNAME;

            File file = new File(compressPath);

            file.mkdirs();

            // 8、将上传的压缩包，存储到解压后的文件夹中
            saveLocalFile(streamWrapper, zipFileName, compressPath);

        }
        FileOutputStream out = getAllFileStream(downloadFilePath, newVersion);
        // 压缩文件
        ZipUtil.toZip(downloadFilePath + newVersion + TEMPLATESTR, out, true);
        // 9、上传压缩包
        uploadNewZipFile(downloadFilePath, newVersion);

        // 6启动一个线程，将刚才的所有文件删除
        DeleteFileThread deleteFileThread = new DeleteFileThread(downloadFilePath);

        new Thread(deleteFileThread).start();

        templateAddBo.setUploadResult(true);
        // 10、返回数据
        return templateAddBo;

    }

    private Boolean checkParam(Integer templateId, String zipFileName, StreamWrapper streamWrapper, TemplateAddBo templateAddBo) {

        if (!zipFileName.endsWith(".zip")) {

            templateAddBo.setUploadResult(false);

            templateAddBo.setUploadMsg("压缩文件的名字" + zipFileName + "不合法，请上传zip压缩文件");

            return false;
        }
        try {
            // 1、先检验压缩包名称是否有重复
            Boolean checkZipResult = checkZipFileName(zipFileName, templateId);

            if (!checkZipResult) {

                templateAddBo.setUploadResult(false);

                templateAddBo.setUploadMsg("压缩文件的名字" + zipFileName + "已存在，请更换名称");

                return false;
            }
        } catch (Exception e) {

            throw new ServiceException("压缩文件不合法，请重新上传合法的zip压缩文件");
        }

        // 2、/**读取zip里面的所有的文件名*/
        List<String> uploadFileNameList = readZipFileNameList(streamWrapper.getInputStream());

        if (CollectionUtils.isEmpty(uploadFileNameList)) {

            templateAddBo.setUploadResult(false);

            templateAddBo.setUploadMsg("压缩包中不存在文件");

            return false;

        }

        // 3、再检验压缩包里面的文件，所有的文件名，是否存在重复的，并取出这些文件名
        List<String> existFileName = checkLuaFileName(uploadFileNameList, templateId);

        if (templateId == null && CollectionUtils.isNotEmpty(existFileName)) {

            String fileNameJoin = StringUtils.join(uploadFileNameList.toArray(), ",");

            templateAddBo.setUploadResult(false);

            templateAddBo.setUploadMsg("主题文件的名字" + fileNameJoin + "已存在，请更换名称");

            return false;
        }


        templateAddBo.setUploadFileNameList(uploadFileNameList);

        return true;
    }


    @Override
    public TemplateAddBo updateUploadFile(MultipartFile fileParam,Integer templateId) {

        String zipFileName = fileParam.getOriginalFilename();

        InputStream uploadStream = null;

        try {
            uploadStream = fileParam.getInputStream();

        } catch (IOException e) {

            log.error("TemplateExtServiceImpl.updateUploadFile ==> get file error!!!.", e);

            throw new ServiceException("获取上传文件报错");

        }
        StreamWrapper streamWrapper = new StreamWrapper(uploadStream);

        TemplateAddBo templateAddBo = new TemplateAddBo();

        Boolean checkRes = checkParam(templateId,zipFileName,streamWrapper,templateAddBo);

        if (!checkRes) {

            return templateAddBo;
        }

        // 4、查询最新模版的版本号
        TbTemplateZipFile tbTemplateZipFile = queryNewestZipFile();

        String downloadFilePath = commonConfig.getFilePath() + UUID.randomUUID() + Constants.SLASH;

        String downLoadFileName = tbTemplateZipFile.getFileVersion() + TEMPLATESTR + Constants.ZIPSUFFIX;

        File downLoadPathFile = new File(downloadFilePath);
        downLoadPathFile.mkdir();
        // 5、下载模版压缩包
        downloadFile(tbTemplateZipFile.getFileName(), downloadFilePath + downLoadFileName);
        // 6、 产生下一个版本号
        String newVersion = getNewVersion(tbTemplateZipFile.getFileVersion());

        templateAddBo.setNewVersion(newVersion);
        //解压的路径
        String compressPath = downloadFilePath + tbTemplateZipFile.getFileVersion() + TEMPLATESTR + Constants.SLASH;

        // 6、解压下载的压缩包
        depressZipFile(downloadFilePath, downLoadFileName);

        // 删除原来的文件
        TbTemplate tbTemplate = tbTemplateMapper.selectByPrimaryKey(templateId);

        String sourceFileName = tbTemplate.getTemplateFileSourceName();

        FileUtil.deleteDir(new File(compressPath + sourceFileName));

        saveLocalFile(streamWrapper, zipFileName, downloadFilePath + tbTemplateZipFile.getFileVersion() + TEMPLATESTR);

        File oldPath = new File(downloadFilePath + tbTemplateZipFile.getFileVersion() + TEMPLATESTR);

        oldPath.renameTo(new File(downloadFilePath + newVersion + TEMPLATESTR));

        FileOutputStream out = getAllFileStream(downloadFilePath, newVersion);
        // 压缩文件
        ZipUtil.toZip(downloadFilePath + newVersion + TEMPLATESTR, out, true);
        // 8、上传压缩包
        uploadNewZipFile(downloadFilePath, newVersion);

        // 6启动一个线程，将刚才的所有文件删除
        InteractionZipFileServiceImpl.DeleteFileThread deleteFileThread = new InteractionZipFileServiceImpl.DeleteFileThread(downloadFilePath);

        new Thread(deleteFileThread).start();

        templateAddBo.setUploadResult(true);

        return templateAddBo;
    }

    @OperationLogAnnotationService(descArgPositions = {0}, fieldNames = {"templateName"}, type = OperationLogTypeEnum._22)
    @Override
    public void updateRelationInfo(TemplateAddBo templateAddBo,Integer templateId) {

        // 1、开启事务 新增模版表，新增模版文件表，新增模版文件压缩表
        transactionTemplate.execute(new TransactionCallback<Boolean>() {

            @Override
            public Boolean doInTransaction(TransactionStatus transactionStatus) {

                try {
                    //先更新 template表，然后更新zip表 删除原来的相关的文件 最后插入file表，

                    TbTemplate temUpdateParam = new TbTemplate();

                    temUpdateParam.setId(templateId);

                    List<String> fileNameList = templateAddBo.getUploadFileNameList();

                    if (StringUtils.isNotEmpty(templateAddBo.getZipFileName())) {

                        List<String> desFileName = fileNameList.stream().filter((K) -> K.endsWith("hotspot.lua")).collect(Collectors.toList());

                        if (CollectionUtils.isEmpty(desFileName) || desFileName.size() > 1) {

                            throw new ServiceException("压缩包中的文件名格式存在问题");
                        }
                        temUpdateParam.setTemplateFileSourceName(templateAddBo.getZipFileName());

                        temUpdateParam.setTemplateFileName(desFileName.get(0));
                    }
                    temUpdateParam.setModifier(templateAddBo.getUsername());

                    temUpdateParam.setInteractionTypeName(templateAddBo.getInteractionTypeName());

                    temUpdateParam.setInteractionId(templateAddBo.getInteractionTypeId());

                    temUpdateParam.setTemplateName(templateAddBo.getTemplateName());

                    tbTemplateMapper.updateByPrimaryKeySelective(temUpdateParam);

                    if (StringUtils.isNotEmpty(templateAddBo.getZipFileName())) {

                        tbTemplateFileMapper.deleteByTemplateId(templateId);

                        for (String fileName : templateAddBo.getUploadFileNameList()) {

                            TbTemplateFile fileParam = new TbTemplateFile();

                            fileParam.setModifier(templateAddBo.getUsername());

                            fileParam.setCreator(templateAddBo.getUsername());

                            fileParam.setFileName(fileName);

                            fileParam.setTemplateId(templateId);

                            fileParam.setIsDeleted("N");

                            tbTemplateFileMapper.insertSelective(fileParam);
                        }
                        TbTemplateZipFile insertParam = new TbTemplateZipFile();

                        insertParam.setFileName(templateAddBo.getNewVersion() + "-template.zip");

                        insertParam.setIsDeleted("N");

                        insertParam.setStatus((byte) 3);

                        insertParam.setModifier(templateAddBo.getUsername());

                        insertParam.setCreator(templateAddBo.getUsername());

                        insertParam.setFileVersion(templateAddBo.getNewVersion());

                        insertParam.setOssurl(commonConfig.getFileDomainName().concat(commonConfig.getPreKey()).concat(insertParam.getFileName()));

                        tbTemplateZipFileMapper.insertSelective(insertParam);

                    }

                    /**
                     * 更新对应素材引用的模板名称
                     * 2018-11-30 by zhangzhewen
                     */
                    tbCreativeMapper.updateTemplateNameByTemplateId(templateId);

                    /**更新fileversion在缓存中的值*/
                    fileVersionCache.setNewVersion(templateAddBo.getNewVersion());

                }catch (ServiceException se) {

                    throw se;

                } catch(Exception e){

                    log.error("TemplateExtServiceImpl.updateRelationInfo ==> update database error!!", e);

                    throw new ServiceException("更新数据库报错，请重新操作");
                }

                return true;
            }
        });

    }

    @Override
    public Boolean addCheckExist(String templateName) {

        TbTemplateCriteria qryParam = new TbTemplateCriteria();

        TbTemplateCriteria.Criteria templateCri = qryParam.createCriteria();

        templateCri.andTemplateNameEqualTo(templateName);

        templateCri.andIsDeletedEqualTo(IsDeletedEnum.NO.getValue());

        List<TbTemplate> tbtemlist = tbTemplateMapper.selectByParam(qryParam);

        if (!CollectionUtils.isEmpty(tbtemlist)) {

            return true;
        }

        return false;
    }

    @Override
    public Boolean updateCheckExist(String templateName, Integer templateId) {

        TbTemplateCriteria qryParam = new TbTemplateCriteria();

        TbTemplateCriteria.Criteria templateCri = qryParam.createCriteria();

        templateCri.andTemplateNameEqualTo(templateName);

        List<TbTemplate> tbtemlist = tbTemplateMapper.selectByParam(qryParam);

        if (!CollectionUtils.isEmpty(tbtemlist)
                && tbtemlist.get(0).getId().intValue() != templateId.intValue()) {

            return true;
        }

        return false;

    }

    private void uploadNewZipFile(String downloadFilePath, String newVersion) {

        InputStream up = null;
        try {

            up = new FileInputStream(new File(downloadFilePath + newVersion + FILENAMESUFFIX));

        } catch (FileNotFoundException e) {

            log.error("TemplateExtServiceImpl.uploadNewZipFile ==> file not found error", e);

            throw new ServiceException("文件操作失败，请重试");
        }

        try {
            commonFileServer.upload(commonConfig.getPreKey() + newVersion + FILENAMESUFFIX, up);

        } catch (Exception e) {

            log.error("TemplateExtServiceImpl.uploadNewZipFile ==> upload file to remote file server error!!", e);

            throw new ServiceException("上传文件到远程文件服务器失败，请重试");
        }


    }

    private FileOutputStream getAllFileStream(String downloadFilePath, String newVersion) {

        try {

            return new FileOutputStream(new File(downloadFilePath + newVersion + FILENAMESUFFIX));

        } catch (FileNotFoundException e) {

            log.error("TemplateExtServiceImpl.getAllFileStream ==> all file Stream error!!", e);

            throw new ServiceException("获取所有的要压缩的文件流失败,请重试");
        }

    }

    private void depressZipFile(String downloadFilePath, String downLoadFileName) {

        try {

            ZipUtil.unzip(new File(downloadFilePath + downLoadFileName), downloadFilePath);

        } catch (Exception e) {

            log.error("TemplateExtServiceImpl.depressZipFile ==> depressZipFile error!!", e);

            throw new ServiceException("解压文件报错");

        }


    }

    private List<String> checkLuaFileName(List<String> uploadFileNameList, Integer templateId) {

        List<TbTemplateFile> fileNameList = tbTemplateFileMapper.selectByFileNameList(uploadFileNameList);

        if (null != templateId && CollectionUtils.isNotEmpty(fileNameList)) {

            List<String> fileNameListStr = fileNameList.stream().filter(k -> k.getId().intValue() != templateId.intValue()).map(TbTemplateFile::getFileName).collect(Collectors.toList());

            return fileNameListStr;
        }

        List<String> fileNameStrList = fileNameList.stream().map(TbTemplateFile::getFileName).collect(Collectors.toList());

        return fileNameStrList;
    }

    private List<String> readZipFileNameList(InputStream inputStream) {
        ZipInputStream zin = new ZipInputStream(inputStream);

        List<String> fileNameList = new ArrayList<>();

        try {
            ZipEntry ze;
            while ((ze = zin.getNextEntry()) != null) {

                if (ze.isDirectory()) {

                    throw new ServiceException("压缩包中不要添加目录");

                } else {

                    fileNameList.add(ze.getName());

                }
            }
            zin.closeEntry();

            if (CollectionUtils.isEmpty(fileNameList)) {

                throw new ServiceException("上传的压缩包中没有文件");

            }
        } catch (IOException ioe) {

            log.error("TemplateExtServiceImpl.readZipFileNameList ==> read zipfile error!!!", ioe);

            throw new ServiceException("读取压缩文件失败,请重试");

        }
        return fileNameList;
    }

    private Boolean checkZipFileName(String zipFileName, Integer templateId) {

        TbTemplateCriteria param = new TbTemplateCriteria();

        TbTemplateCriteria.Criteria cri = param.createCriteria();

        cri.andTemplateFileSourceNameEqualTo(zipFileName).andIsDeletedEqualTo("N");

        List<TbTemplate> templateList = tbTemplateMapper.selectByParam(param);

        if (null != templateId && CollectionUtils.isNotEmpty(templateList)) {

            for (TbTemplate tbTemplate : templateList) {

                if (tbTemplate.getId().intValue() != templateId.intValue()) {

                    return false;
                }

            }

            return true;

        }

        if (CollectionUtils.isNotEmpty(templateList)) {

            return false;
        } else {

            return true;
        }

    }

    /**
     * 新增模版的流程
     */
    @OperationLogAnnotationService(descArgPositions = {0}, fieldNames = {"templateName"}, type = OperationLogTypeEnum._21)
    @Override
    public void addTemplateInfo(TemplateAddBo templateAddBo) {

        // 1、开启事务 新增模版表，新增模版文件表，新增模版文件压缩表
        transactionTemplate.execute(new TransactionCallback<Boolean>() {

            @Override
            public Boolean doInTransaction(TransactionStatus transactionStatus) {

                TbTemplate templateParam = new TbTemplate();

                List<String> fileNameList = templateAddBo.getUploadFileNameList();

                List<String> desFileName = fileNameList.stream().filter((K) -> K.endsWith("hotspot.lua")).collect(Collectors.toList());

                if (CollectionUtils.isEmpty(desFileName) || desFileName.size() > 1) {

                    throw new ServiceException("压缩包中的文件名格式存在问题");
                }
                templateParam.setTemplateFileSourceName(templateAddBo.getZipFileName());

                templateParam.setIsDeleted("N");

                templateParam.setTemplateName(templateAddBo.getTemplateName());

                templateParam.setInteractionId(templateAddBo.getInteractionTypeId());

                templateParam.setCreator(templateAddBo.getUsername());

                templateParam.setInteractionTypeName(templateAddBo.getInteractionTypeName());

                templateParam.setModifier(templateAddBo.getUsername());

                templateParam.setTemplateFileName(desFileName.get(0));
                try {

                    tbTemplateMapper.insertSelective(templateParam);

                    for (String fileName : fileNameList) {

                        TbTemplateFile fileParam = new TbTemplateFile();

                        fileParam.setModifier(templateAddBo.getUsername());

                        fileParam.setCreator(templateAddBo.getUsername());

                        fileParam.setFileName(fileName);

                        fileParam.setTemplateId(templateParam.getId());

                        fileParam.setIsDeleted("N");

                        tbTemplateFileMapper.insertSelective(fileParam);
                    }
                    TbTemplateZipFile zipFileParam = new TbTemplateZipFile();

                    zipFileParam.setFileName(templateAddBo.getNewVersion() + "-template.zip");

                    zipFileParam.setIsDeleted("N");

                    zipFileParam.setStatus((byte) 3);

                    zipFileParam.setModifier(templateAddBo.getUsername());

                    zipFileParam.setCreator(templateAddBo.getUsername());

                    zipFileParam.setFileVersion(templateAddBo.getNewVersion());

                    zipFileParam.setOssurl(commonConfig.getFileDomainName().concat(commonConfig.getPreKey()).concat(zipFileParam.getFileName()));

                    tbTemplateZipFileMapper.insertSelective(zipFileParam);

                    fileVersionCache.setNewVersion(templateAddBo.getNewVersion());

                } catch (Exception e) {

                    log.error("TemplateExtServiceImpl.addTemplateInfo ==> insert info error!!!", e);

                    throw new ServiceException("插入数据报错");
                }
                return true;
            }
        });


    }


}
