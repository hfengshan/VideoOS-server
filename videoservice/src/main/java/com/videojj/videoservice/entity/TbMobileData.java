package com.videojj.videoservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

import java.util.Date;

@Builder
@AllArgsConstructor
@ToString
public class TbMobileData {
    private Integer id;

    private String userId;

    private Integer creativeId;

    private String businessInfo;

    private String extraInfo;

    private String advId;

    private String creator;

    private Date gmtCreated;

    private String modifier;

    private Date gmtModified;

    private String isDeleted;

    public TbMobileData(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Integer getCreativeId() {
        return creativeId;
    }

    public void setCreativeId(Integer creativeId) {
        this.creativeId = creativeId;
    }

    public String getBusinessInfo() {
        return businessInfo;
    }

    public void setBusinessInfo(String businessInfo) {
        this.businessInfo = businessInfo == null ? null : businessInfo.trim();
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo == null ? null : extraInfo.trim();
    }

    public String getAdvId() {
        return advId;
    }

    public void setAdvId(String advId) {
        this.advId = advId == null ? null : advId.trim();
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