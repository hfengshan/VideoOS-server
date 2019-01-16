package com.videojj.videoservice.bo;

import com.videojj.videoservice.task.LoadCacheTask;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author wangpeng@videopls.com
 * Created by wangpeng on 2018/12/4 下午6:45.
 * @Description:
 */
@Getter
@Setter
@ToString
public class LoadCacheBo {

    private String jobName;

    private String jobCron;

    private Integer launchPlanId;

    private LoadCacheTask loadCacheTask;
}
