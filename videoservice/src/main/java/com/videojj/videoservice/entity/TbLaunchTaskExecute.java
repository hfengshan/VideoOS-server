package com.videojj.videoservice.entity;

import java.util.Date;

public class TbLaunchTaskExecute {
    private Integer id;

    private String executeName;

    private Integer launchPlanId;

    private Date executeTime;

    private Byte executeStatus;

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

    public String getExecuteName() {
        return executeName;
    }

    public void setExecuteName(String executeName) {
        this.executeName = executeName == null ? null : executeName.trim();
    }

    public Integer getLaunchPlanId() {
        return launchPlanId;
    }

    public void setLaunchPlanId(Integer launchPlanId) {
        this.launchPlanId = launchPlanId;
    }

    public Date getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Date executeTime) {
        this.executeTime = executeTime;
    }

    public Byte getExecuteStatus() {
        return executeStatus;
    }

    public void setExecuteStatus(Byte executeStatus) {
        this.executeStatus = executeStatus;
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