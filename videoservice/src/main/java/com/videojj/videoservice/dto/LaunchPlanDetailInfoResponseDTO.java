package com.videojj.videoservice.dto;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/16 下午5:04.
 * @Description:
 */
@Getter
@Setter
@ToString
public class LaunchPlanDetailInfoResponseDTO {

    private String interactionTypeName;

    private Integer interactionTypeId;

    private String launchPlanName;

    private Integer creativeId;

    private String creativeName;

    private String launchVideoId;

    private Byte launchTimeType;

    private String launchDateStart;

    private String launchDateEnd;

    private List<List<String>> launchTime;

    private String launchLenTime;

    private Byte status;

    private String remark;

    private String resCode;

    private String resMsg;

    private String attachInfo;

    private Integer hotSpotNum;

    private List<MonitorLinkDTO> hotspotTrackLink;

    private List<MonitorLinkDTO> infoTrackLink;
}
