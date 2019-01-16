package com.videojj.videoservice.bo;

import com.videojj.videoservice.handler.SendEmqService;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/31 下午2:52.
 * @Description:
 */
@Getter
@Setter
@ToString
public class LaunchPlanTaskBo {


    private String jobName;

    private String jobCron;

    private Integer launchPlanId;

    private SendEmqService sendEmqService;
}
