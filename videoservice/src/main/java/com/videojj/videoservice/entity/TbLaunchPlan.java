package com.videojj.videoservice.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "`tb_launch_plan`")
public class TbLaunchPlan {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 投放计划名称
     */
    @Column(name = "`launch_plan_name`")
    private String launchPlanName;

    /**
     * 类型id
     */
    @Column(name = "`interaction_id`")
    private Integer interactionId;

    /**
     * 素材id
     */
    @Column(name = "`creative_id`")
    private Integer creativeId;

    /**
     * 镜像的id
     */
    @Column(name = "`creative_image_id`")
    private Integer creativeImageId;

    /**
     * 投放视频id
     */
    @Column(name = "`launch_video_id`")
    private String launchVideoId;

    /**
     * 0:视频时间 1:即时 2:北京时间
     */
    @Column(name = "`launch_time_type`")
    private Byte launchTimeType;

    /**
     * 投放开始日期
     */
    @Column(name = "`launch_date_start`")
    private Date launchDateStart;

    /**
     * 投放结束日期
     */
    @Column(name = "`launch_date_end`")
    private Date launchDateEnd;

    /**
     * 投放时间
     */
    @Column(name = "`launch_time`")
    private String launchTime;

    /**
     * 投放时长
     */
    @Column(name = "`launch_len_time`")
    private String launchLenTime;

    /**
     * 审核状态 0:审核未通过 1:审核通过
     */
    @Column(name = "`status`")
    private Byte status;

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
     * 投放计划操作表外键
     */
    @Column(name = "`operation_id`")
    private Integer operationId;

    /**
     * 热点监控链接信息
     */
    @Column(name = "`hotspot_track_link`")
    private String hotspotTrackLink;

    /**
     * 信息层监控链接
     */
    @Column(name = "`info_track_link`")
    private String infoTrackLink;

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
     * 获取投放计划名称
     *
     * @return launch_plan_name - 投放计划名称
     */
    public String getLaunchPlanName() {
        return launchPlanName;
    }

    /**
     * 设置投放计划名称
     *
     * @param launchPlanName 投放计划名称
     */
    public void setLaunchPlanName(String launchPlanName) {
        this.launchPlanName = launchPlanName == null ? null : launchPlanName.trim();
    }

    /**
     * 获取类型id
     *
     * @return interaction_id - 类型id
     */
    public Integer getInteractionId() {
        return interactionId;
    }

    /**
     * 设置类型id
     *
     * @param interactionId 类型id
     */
    public void setInteractionId(Integer interactionId) {
        this.interactionId = interactionId;
    }

    /**
     * 获取素材id
     *
     * @return creative_id - 素材id
     */
    public Integer getCreativeId() {
        return creativeId;
    }

    /**
     * 设置素材id
     *
     * @param creativeId 素材id
     */
    public void setCreativeId(Integer creativeId) {
        this.creativeId = creativeId;
    }

    /**
     * 获取镜像的id
     *
     * @return creative_image_id - 镜像的id
     */
    public Integer getCreativeImageId() {
        return creativeImageId;
    }

    /**
     * 设置镜像的id
     *
     * @param creativeImageId 镜像的id
     */
    public void setCreativeImageId(Integer creativeImageId) {
        this.creativeImageId = creativeImageId;
    }

    /**
     * 获取投放视频id
     *
     * @return launch_video_id - 投放视频id
     */
    public String getLaunchVideoId() {
        return launchVideoId;
    }

    /**
     * 设置投放视频id
     *
     * @param launchVideoId 投放视频id
     */
    public void setLaunchVideoId(String launchVideoId) {
        this.launchVideoId = launchVideoId == null ? null : launchVideoId.trim();
    }

    /**
     * 获取0:视频时间 1:即时 2:北京时间
     *
     * @return launch_time_type - 0:视频时间 1:即时 2:北京时间
     */
    public Byte getLaunchTimeType() {
        return launchTimeType;
    }

    /**
     * 设置0:视频时间 1:即时 2:北京时间
     *
     * @param launchTimeType 0:视频时间 1:即时 2:北京时间
     */
    public void setLaunchTimeType(Byte launchTimeType) {
        this.launchTimeType = launchTimeType;
    }

    /**
     * 获取投放开始日期
     *
     * @return launch_date_start - 投放开始日期
     */
    public Date getLaunchDateStart() {
        return launchDateStart;
    }

    /**
     * 设置投放开始日期
     *
     * @param launchDateStart 投放开始日期
     */
    public void setLaunchDateStart(Date launchDateStart) {
        this.launchDateStart = launchDateStart;
    }

    /**
     * 获取投放结束日期
     *
     * @return launch_date_end - 投放结束日期
     */
    public Date getLaunchDateEnd() {
        return launchDateEnd;
    }

    /**
     * 设置投放结束日期
     *
     * @param launchDateEnd 投放结束日期
     */
    public void setLaunchDateEnd(Date launchDateEnd) {
        this.launchDateEnd = launchDateEnd;
    }

    /**
     * 获取投放时间
     *
     * @return launch_time - 投放时间
     */
    public String getLaunchTime() {
        return launchTime;
    }

    /**
     * 设置投放时间
     *
     * @param launchTime 投放时间
     */
    public void setLaunchTime(String launchTime) {
        this.launchTime = launchTime == null ? null : launchTime.trim();
    }

    /**
     * 获取投放时长
     *
     * @return launch_len_time - 投放时长
     */
    public String getLaunchLenTime() {
        return launchLenTime;
    }

    /**
     * 设置投放时长
     *
     * @param launchLenTime 投放时长
     */
    public void setLaunchLenTime(String launchLenTime) {
        this.launchLenTime = launchLenTime == null ? null : launchLenTime.trim();
    }

    /**
     * 获取审核状态 0:审核未通过 1:审核通过
     *
     * @return status - 审核状态 0:审核未通过 1:审核通过
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置审核状态 0:审核未通过 1:审核通过
     *
     * @param status 审核状态 0:审核未通过 1:审核通过
     */
    public void setStatus(Byte status) {
        this.status = status;
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
     * 获取投放计划操作表外键
     *
     * @return operation_id - 投放计划操作表外键
     */
    public Integer getOperationId() {
        return operationId;
    }

    /**
     * 设置投放计划操作表外键
     *
     * @param operationId 投放计划操作表外键
     */
    public void setOperationId(Integer operationId) {
        this.operationId = operationId;
    }

    /**
     * 获取热点监控链接信息
     *
     * @return hotspot_track_link - 热点监控链接信息
     */
    public String getHotspotTrackLink() {
        return hotspotTrackLink;
    }

    /**
     * 设置热点监控链接信息
     *
     * @param hotspotTrackLink 热点监控链接信息
     */
    public void setHotspotTrackLink(String hotspotTrackLink) {
        this.hotspotTrackLink = hotspotTrackLink == null ? null : hotspotTrackLink.trim();
    }

    /**
     * 获取信息层监控链接
     *
     * @return info_track_link - 信息层监控链接
     */
    public String getInfoTrackLink() {
        return infoTrackLink;
    }

    /**
     * 设置信息层监控链接
     *
     * @param infoTrackLink 信息层监控链接
     */
    public void setInfoTrackLink(String infoTrackLink) {
        this.infoTrackLink = infoTrackLink == null ? null : infoTrackLink.trim();
    }
}