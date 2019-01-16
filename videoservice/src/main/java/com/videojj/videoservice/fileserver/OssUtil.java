package com.videojj.videoservice.fileserver;

import com.aliyun.openservices.ClientException;
import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.OSSException;
import com.aliyun.openservices.oss.model.OSSObject;
import com.aliyun.openservices.oss.model.ObjectMetadata;
import com.aliyun.openservices.oss.model.PutObjectResult;
import com.google.common.base.Preconditions;
import com.videojj.videoservice.config.FileServerConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * OssUtils
 *
 */
@Slf4j
@Getter
@Setter
public class OssUtil implements CommonFileServer{

    private FileServerConfig ossConfig;

    public OssUtil(FileServerConfig ossConfig){

        this.ossConfig = ossConfig;
    }


    @Override
    public InputStream download(String url) {

        return downloadFileFromVideoOSS(url);

    }

    @Override
    public void upload(String url, InputStream in) throws Exception{

        uploadFile2OSS(url,in);
    }

    @Override
    public void delete(String url) throws Exception{

        deleteFile2OSS(url);

    }


    public OSSClient getVideoOSSClient() {
        OSSClient ossClient =new OSSClient(ossConfig.getEndpoint(),ossConfig.getAccessKey(),ossConfig.getSecret());
        return ossClient;
    }



    /**
     * oss下载
     *
     * @param objectkey
     * @return
     */
    public InputStream downloadFileFromVideoOSS(String objectkey) {

        Preconditions.checkArgument(StringUtils.isNotBlank(ossConfig.getBucketName()), "param bucketName is empty");

        Preconditions.checkArgument(StringUtils.isNotBlank(objectkey), "param objectkey is empty");

        try {
            OSSClient videoOSSClient=getVideoOSSClient();
            log.info("ossClient:{}", videoOSSClient.toString());

            OSSObject result = videoOSSClient.getObject(ossConfig.getBucketName(), objectkey);

            InputStream inputStream = (null != result) ? result.getObjectContent() : null;

            return inputStream;

        } catch (OSSException e) {

            log.error("Unable to download File From OSS. Occur OSSException : {}", e);

            throw e;

        } catch (ClientException e) {

            log.error("Unable to download File From OSS. Occur ClientException : {}", e);

            throw e;

        } catch (Exception e) {

            log.error("Unable to download File From OSS. Occur Exception : {}", e);

            throw e;

        }

    }

    /**
     * uploadFileOSS
     *
     * @param key
     * @param ins
     * @return
     * @throws Exception
     */
    public boolean uploadFile2OSS(String key, InputStream ins) throws Exception {

        try {
            OSSClient videoOSSClient=getVideoOSSClient();
            ObjectMetadata objectMeta = new ObjectMetadata();

            objectMeta.setContentLength(ins.available());

            objectMeta.setContentEncoding("UTF-8");

            if (!videoOSSClient.doesBucketExist(ossConfig.getBucketName())) {

                videoOSSClient.createBucket(ossConfig.getBucketName());
            }

            PutObjectResult putObject = videoOSSClient.putObject(ossConfig.getBucketName(), key, ins, objectMeta);

            if (StringUtils.isBlank(putObject.getETag())) {

                return false;

            }

            return true;

        } catch (OSSException e) {

            log.error("Unable to upload file to OSS. Occur OSSException: {}", e);

            throw e;

        } catch (ClientException e) {

            log.error("Unable to upload file to OSS. Occur ClientException: {}", e);

            throw e;

        } catch (IOException e) {

            log.error("Unable to upload file to OSS. Occur IOException : {}", e);

            throw e;

        } catch (Exception e) {

            log.error("Unable to upload file to OSS. Occur Exception : {}", e);

            throw e;

        } finally {

            if (ins != null) {

                try {

                    ins.close();

                } catch (IOException e) {

                    log.error("Unable to close InputStream. Occur IOException : {}", e);

                }

            }
        }

    }

    /**
     * uploadFileOSS
     *
     * @param key
     * @param
     * @return
     * @throws Exception
     */
    public boolean deleteFile2OSS(String key) throws Exception {

        try {
            OSSClient videoOSSClient=getVideoOSSClient();
            ObjectMetadata objectMeta = new ObjectMetadata();

            objectMeta.setContentEncoding("UTF-8");

            if (!videoOSSClient.doesBucketExist(ossConfig.getBucketName())) {

                videoOSSClient.createBucket(ossConfig.getBucketName());
            }

            videoOSSClient.deleteObject(ossConfig.getBucketName(),key);


            return true;

        } catch (OSSException e) {

            log.error("Unable to delete file to OSS. Occur OSSException: {}", e);

            throw e;

        } catch (ClientException e) {

            log.error("Unable to delete file to OSS. Occur ClientException: {}", e);

            throw e;

        } catch (Exception e) {

            log.error("Unable to delete file to OSS. Occur Exception : {}", e);

            throw e;

        }

    }


}
