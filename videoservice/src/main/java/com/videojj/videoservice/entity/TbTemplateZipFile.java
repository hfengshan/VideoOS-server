package com.videojj.videoservice.entity;

import java.util.Date;

public class TbTemplateZipFile {
    private Integer id;

    private String fileName;

    private String fileVersion;

    private String ossurl;

    private Byte status;

    private String extraInfo;

    private String creator;

    private Date gmtCreated;

    private String modifier;

    private Date gmtModified;

    private String isDeleted;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    public String getFileVersion() {
        return fileVersion;
    }

    public void setFileVersion(String fileVersion) {
        this.fileVersion = fileVersion == null ? null : fileVersion.trim();
    }

    public String getOssurl() {
        return ossurl;
    }

    public void setOssurl(String ossurl) {
        this.ossurl = ossurl == null ? null : ossurl.trim();
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo == null ? null : extraInfo.trim();
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public Date getGmtCreated() {
        return gmtCreated;
    }

    public void setGmtCreated(Date gmtCreated) {
        this.gmtCreated = gmtCreated;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier == null ? null : modifier.trim();
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted == null ? null : isDeleted.trim();
    }
}