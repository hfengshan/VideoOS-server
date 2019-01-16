package com.videojj.videoservice.service.impl;

import com.videojj.videocommon.constant.Constants;
import com.videojj.videocommon.exception.ServiceException;
import com.videojj.videoservice.config.CommonConfig;
import com.videojj.videoservice.dao.TbTemplateFileMapper;
import com.videojj.videoservice.dao.TbTemplateMapper;
import com.videojj.videoservice.dao.TbTemplateZipFileMapper;
import com.videojj.videoservice.entity.TbTemplateZipFile;
import com.videojj.videoservice.entity.TbTemplateZipFileCriteria;
import com.videojj.videoservice.enums.IsDeletedEnum;
import com.videojj.videoservice.fileserver.CommonFileServer;
import com.videojj.videoservice.service.OperationLogService;
import com.videojj.videoservice.util.FileUtil;
import com.videojj.videoservice.util.StreamWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @Author @videopls.com
 * Created by  on 2018/10/30 下午7:44.
 * @Description:
 */
public abstract class AbstractTemplateService {


    @Resource
    protected CommonFileServer commonFileServer;

    @Resource
    protected TbTemplateZipFileMapper tbTemplateZipFileMapper;

    @Resource
    protected CommonConfig commonConfig;

    @Resource
    protected TransactionTemplate transanctionTemplate;

    @Resource
    protected TbTemplateMapper tbTemplateMapper;

    @Resource
    protected OperationLogService operationLogService;

    @Resource
    protected TbTemplateFileMapper tbTemplateFileMapper;


    protected final String STARTVERSIONPRE = "1.0.";

    protected final String STARTVERSION = "1.0.0";

    protected final String FILENAMESUFFIX = "-template.zip";

    protected final String TEMPLATESTR = "-template";

    protected final String INITFILEVERSIONNAME = "1.0.0-template/";

    private static Logger log = LoggerFactory.getLogger("AbstractTemplateService");

    /**原方法*/
    public TbTemplateZipFile queryNewestZipFile() {

        TbTemplateZipFileCriteria tbTemplateZipFileCriteria = new TbTemplateZipFileCriteria();

        TbTemplateZipFileCriteria.Criteria crit = tbTemplateZipFileCriteria.createCriteria();

        crit.andIsDeletedEqualTo(IsDeletedEnum.NO.getValue());

        tbTemplateZipFileCriteria.setOrderByClause(Constants.CREATEDDESC);

        List<TbTemplateZipFile> zipFileList = tbTemplateZipFileMapper.selectByParam(tbTemplateZipFileCriteria);

        if(CollectionUtils.isEmpty(zipFileList)){

            return null;
        }else{

            return zipFileList.get(0);
        }

    }
    /**需要抽象出来的方法*/
    protected void downloadFile(String fileName,String zipFileFullPath) {

        /**下载zip文件并保存到本地*/
        InputStream zipInputStream = null;
        FileOutputStream fileOutput = null;
        try {
            zipInputStream = commonFileServer.download(commonConfig.getPreKey()+fileName);

            fileOutput = new FileOutputStream(zipFileFullPath);

            byte data[] = new byte[1024];

            int count = 0;

            while ((count = zipInputStream.read(data, 0, data.length)) != -1) {
                fileOutput.write(data, 0, count);
            }

        }catch (IOException ioe){

            log.error("AbstractTemplateService.downloadFile ==> download file from oss IOError",ioe);

            throw new ServiceException("下载文件的时候IO报错");
        }catch (Exception e){

            log.error("AbstractTemplateService.downloadFile ==> download file from oss unknow Error",e);

            throw new ServiceException("下载文件的时候报错");
        }finally {

            try {
                fileOutput.close();
                zipInputStream.close();
            } catch (IOException e) {

                log.error("AbstractTemplateService.downloadFile ==> close stream error!!",e);
            }

        }
    }

    /**可以共用的方法*/
    protected void saveLocalFile(StreamWrapper streamWrapper, String generatedFileName, String compressPath){

        FileOutputStream fileOutputStream = null;

        InputStream inputStream = null;
        try {
            /**将文件添加进去*/
            byte[] data = new byte[1024];
            int len = 0;
            /**上面的步骤，其实可以和更新共用的代码*/
            File nfile = new File(compressPath +Constants.SLASH+ generatedFileName);

            if(!nfile.exists()){

                nfile.createNewFile();
            }

            fileOutputStream = new FileOutputStream(nfile);

            inputStream = streamWrapper.getInputStream();

            while ((len = inputStream.read(data)) != -1) {

                fileOutputStream.write(data, 0, len);

            }

        }catch (IOException ioe){

            log.error("AbstractTemplateService.downloadFile ==> copy stream to file IOerror",ioe);

            throw new ServiceException("文件操作失败，请重试");
        }catch (Exception e){

            log.error("AbstractTemplateService.downloadFile ==> copy stream to file unknow error",e);

            throw new ServiceException("文件操作失败，请重试");
        }finally {

            try {

                fileOutputStream.close();

                inputStream.close();
            } catch (IOException e) {
            }

        }
    }
    public static class DeleteFileThread implements Runnable{

        private String filePath;

        public DeleteFileThread(String filePath){

            this.filePath = filePath;

        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        @Override
        public void run() {

            FileUtil.deleteDir(new File(filePath));

        }
    }

    /**需要抽象出来的方法*/
    protected String getNewVersion(String fileVersion) {

        if(StringUtils.isEmpty(fileVersion)){

            return STARTVERSION;
        }

        Integer versionNum = Integer.parseInt(fileVersion.split("\\.")[2])+1;

        return STARTVERSIONPRE.concat(versionNum.toString());

    }
}
