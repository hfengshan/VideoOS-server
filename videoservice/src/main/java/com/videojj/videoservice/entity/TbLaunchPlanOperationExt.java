package com.videojj.videoservice.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author //@videopls.com
 * Created by // on 2018/8/8 下午3:09.
 * @Description:
 */
@Getter
@Setter
@ToString
public class TbLaunchPlanOperationExt extends TbLaunchPlanOperation{

    private String interactionName;

    private String creativeName;

    private String launchVideoId;
}
