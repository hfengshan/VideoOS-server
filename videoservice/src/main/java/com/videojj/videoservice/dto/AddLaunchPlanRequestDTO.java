package com.videojj.videoservice.dto;

import com.alibaba.fastjson.JSON;
import com.videojj.videoservice.validation.annotation.IsConflictLaunchPlan;
import com.videojj.videoservice.validation.group.GroupSequence1;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/7 下午6:52.
 * @Description:
 */
@Getter
@Setter
@ToString
@IsConflictLaunchPlan
@GroupSequence({GroupSequence1.class, AddLaunchPlanRequestDTO.class})
public class AddLaunchPlanRequestDTO {

    @NotBlank(message = "{com.videojj.validation.NotBlank.launchPlanName.message}",groups = GroupSequence1.class)
    private String  launchPlanName;

    @NotNull(message = "{com.videojj.validation.NotNull.creativeId.message}",groups = GroupSequence1.class)
    private Integer  creativeId;

    @NotBlank(message = "{com.videojj.validation.NotBlank.launchVideoId.message}",groups = GroupSequence1.class)
    private String  launchVideoId;

    @NotNull(message = "{com.videojj.validation.NotNull.launchTimeType.message}",groups = GroupSequence1.class)
    private Byte  launchTimeType;

//    @NotBlank(message = "{com.videojj.validation.NotBlank.launchDateStart.message}",groups = GroupSequence1.class)
    private String launchDateStart;

//    @NotBlank(message = "{com.videojj.validation.NotBlank.launchDateEnd.message}",groups = GroupSequence1.class)
    private String launchDateEnd;

//    @NotEmpty(message = "{com.videojj.validation.NotBlank.launchTime.message}",groups = GroupSequence1.class)
    private  List<List<String>> launchTime;

    private String  launchLenTime;

    @NotNull(message = "{com.videojj.validation.NotNull.interactionTypeId.message}",groups = GroupSequence1.class)
    private Integer interactionTypeId;

    private List<MonitorLinkDTO> hotspotTrackLink;

    private List<MonitorLinkDTO> infoTrackLink;

    private String infoTrackLinkTitle;

    private String username;


}
