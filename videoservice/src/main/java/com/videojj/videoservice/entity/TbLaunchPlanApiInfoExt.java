package com.videojj.videoservice.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/24 下午2:37.
 * @Description:
 */
@Getter
@Setter
@ToString
public class TbLaunchPlanApiInfoExt {

    private String templateFileName;

    private String launchVideoId;

    private Date launchDateStart;

    private Date launchDateEnd;

    private String launchTime;

    private String launchLenTime;

    private String materialContent;

    private Byte launchTimeType;

    private Integer sourceId;

    private Integer hotSpotNum;

    private String hotspotTrackLink;

    private String infoTrackLink;

    private Integer launchPlanId;

}
