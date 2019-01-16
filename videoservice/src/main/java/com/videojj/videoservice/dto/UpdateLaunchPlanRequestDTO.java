package com.videojj.videoservice.dto;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * @Author //@videopls.com
 * Created by // on 2018/8/7 下午7:31.
 * @Description:
 */
@Getter
@Setter
@ToString
public class UpdateLaunchPlanRequestDTO {

    private String  launchPlanName;

    private Integer  creativeId;

    private String  launchVideoId;

    private Byte  launchTimeType;

    private String launchDateStart;

    private String launchDateEnd;

    private List<List<String>> launchTime;

    private String  launchLenTime;

    private Integer interactionTypeId;

    private String username;

    private Integer launchPlanId;
}
