package com.videojj.videoservice.entity;

import java.util.Date;

public class TbCreative {
    protected Integer id;

    protected String materialName;

    protected Integer interactionId;

    protected Integer templateId;

    protected Byte status;

    protected String extraInfo;

    protected String creator;

    protected Date gmtCreated;

    protected String modifier;

    protected Date gmtModified;

    protected String isDeleted;

    protected String materialContent;

    private String interactionName;

    private String templateName;

    private Integer hotSpotNum;

    public Integer getHotSpotNum() {
        return hotSpotNum;
    }

    public void setHotSpotNum(Integer hotSpotNum) {
        this.hotSpotNum = hotSpotNum;
    }

    public String getInteractionName() {
        return interactionName;
    }

    public void setInteractionName(String interactionName) {
        this.interactionName = interactionName;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Integer getInteractionId() {
        return interactionId;
    }

    public void setInteractionId(Integer interactionId) {
        this.interactionId = interactionId;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName == null ? null : materialName.trim();
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

    public String getMaterialContent() {
        return materialContent;
    }

    public void setMaterialContent(String materialContent) {
        this.materialContent = materialContent == null ? null : materialContent.trim();
    }

}