package com.videojj.videoservice.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "`tb_interaction`")
public class TbInteraction {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 类型名称
     */
    @Column(name = "`interaction_type_name`")
    private String interactionTypeName;

    /**
     * 文件名称
     */
    @Column(name = "`file_name`")
    private String fileName;

    /**
     * 文件内容
     */
    @Column(name = "`content`")
    private String content;

    /**
     * 扩展信息
     */
    @Column(name = "`extra_info`")
    private String extraInfo;

    /**
     * 创建人
     */
    @Column(name = "`creator`")
    private String creator;

    /**
     * 创建时间
     */
    @Column(name = "`gmt_created`")
    private Date gmtCreated;

    /**
     * 修改人
     */
    @Column(name = "`MODIFIER`")
    private String modifier;

    /**
     * 最后修改时间
     */
    @Column(name = "`gmt_modified`")
    private Date gmtModified;

    /**
     * 是否删除
     */
    @Column(name = "`is_deleted`")
    private String isDeleted;

    /**
     * 是否系统应用
     */
    @Column(name = "`is_system`")
    private String isSystem;

    /**
     * 图片url
     */
    @Column(name = "`img_url`")
    private String imgUrl;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取类型名称
     *
     * @return interaction_type_name - 类型名称
     */
    public String getInteractionTypeName() {
        return interactionTypeName;
    }

    /**
     * 设置类型名称
     *
     * @param interactionTypeName 类型名称
     */
    public void setInteractionTypeName(String interactionTypeName) {
        this.interactionTypeName = interactionTypeName == null ? null : interactionTypeName.trim();
    }

    /**
     * 获取文件名称
     *
     * @return file_name - 文件名称
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * 设置文件名称
     *
     * @param fileName 文件名称
     */
    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    /**
     * 获取文件内容
     *
     * @return content - 文件内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置文件内容
     *
     * @param content 文件内容
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    /**
     * 获取扩展信息
     *
     * @return extra_info - 扩展信息
     */
    public String getExtraInfo() {
        return extraInfo;
    }

    /**
     * 设置扩展信息
     *
     * @param extraInfo 扩展信息
     */
    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo == null ? null : extraInfo.trim();
    }

    /**
     * 获取创建人
     *
     * @return creator - 创建人
     */
    public String getCreator() {
        return creator;
    }

    /**
     * 设置创建人
     *
     * @param creator 创建人
     */
    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    /**
     * 获取创建时间
     *
     * @return gmt_created - 创建时间
     */
    public Date getGmtCreated() {
        return gmtCreated;
    }

    /**
     * 设置创建时间
     *
     * @param gmtCreated 创建时间
     */
    public void setGmtCreated(Date gmtCreated) {
        this.gmtCreated = gmtCreated;
    }

    /**
     * 获取修改人
     *
     * @return MODIFIER - 修改人
     */
    public String getModifier() {
        return modifier;
    }

    /**
     * 设置修改人
     *
     * @param modifier 修改人
     */
    public void setModifier(String modifier) {
        this.modifier = modifier == null ? null : modifier.trim();
    }

    /**
     * 获取最后修改时间
     *
     * @return gmt_modified - 最后修改时间
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * 设置最后修改时间
     *
     * @param gmtModified 最后修改时间
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    /**
     * 获取是否删除
     *
     * @return is_deleted - 是否删除
     */
    public String getIsDeleted() {
        return isDeleted;
    }

    /**
     * 设置是否删除
     *
     * @param isDeleted 是否删除
     */
    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted == null ? null : isDeleted.trim();
    }

    /**
     * 获取是否系统应用
     *
     * @return is_system - 是否系统应用
     */
    public String getIsSystem() {
        return isSystem;
    }

    /**
     * 设置是否系统应用
     *
     * @param isSystem 是否系统应用
     */
    public void setIsSystem(String isSystem) {
        this.isSystem = isSystem == null ? null : isSystem.trim();
    }

    /**
     * 获取图片url
     *
     * @return img_url - 图片url
     */
    public String getImgUrl() {
        return imgUrl;
    }

    /**
     * 设置图片url
     *
     * @param imgUrl 图片url
     */
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl == null ? null : imgUrl.trim();
    }
}