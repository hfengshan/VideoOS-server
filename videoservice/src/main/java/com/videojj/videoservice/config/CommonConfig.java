package com.videojj.videoservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author //@videopls.com
 * Created by // on 2018/8/1 下午3:45.
 * @Description:
 */
@Component
@ConfigurationProperties(prefix = "video.common")
public class CommonConfig {

    private String filePath;

    private String preKey;

    private String fileTool;

    private String fileDomainName;

    public String aesKey;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getPreKey() {
        return preKey;
    }

    public void setPreKey(String preKey) {
        this.preKey = preKey;
    }

    public String getFileTool() {
        return fileTool;
    }

    public void setFileTool(String fileTool) {
        this.fileTool = fileTool;
    }

    public String getFileDomainName() {
        return fileDomainName;
    }

    public void setFileDomainName(String fileDomainName) {
        this.fileDomainName = fileDomainName;
    }

    public String getAesKey() {
        return aesKey;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }
}
