package com.videojj.videoservice.task;

import com.videojj.videoservice.bo.LoadCacheBo;
import com.videojj.videoservice.handler.LoadCacheJob;
import com.videojj.videoservice.quartz.QuartzManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Author @videopls.com
 * Created by  on 2018/12/4 下午5:21.
 * @Description:
 */
@Component
public class LoadCacheQuartz {

    @Resource
    private LoadCacheTask loadCacheTask;

    private final String jobNamePre = "loadCache";

    @Value("${video.common.loadcachecron}")
    private String cronExc;

    public void addJob() {

        LoadCacheBo loadCacheBo = new LoadCacheBo();

        loadCacheBo.setLoadCacheTask(loadCacheTask);

        QuartzManager.addJob(jobNamePre.concat("-").concat(new Date().toString()), LoadCacheJob.class,cronExc,loadCacheBo);

    }
}
