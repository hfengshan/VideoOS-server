package com.videojj.videoservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

import java.util.Date;

@Builder
@AllArgsConstructor
@ToString
public class TbOperationLog {
    private Integer id;

    private String operationDesc;

    private Integer operationType;

    private String creator;

    private Date gmtCreated;

    private String modifier;

    private Date gmtModified;

    private String isDeleted;

    public TbOperationLog(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOperationDesc() {
        return operationDesc;
    }

    public void setOperationDesc(String operationDesc) {
        this.operationDesc = operationDesc == null ? null : operationDesc.trim();
    }

    public Integer getOperationType() {
        return operationType;
    }

    public void setOperationType(Integer operationType) {
        this.operationType = operationType;
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