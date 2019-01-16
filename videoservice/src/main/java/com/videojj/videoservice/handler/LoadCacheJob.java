package com.videojj.videoservice.handler;

import com.videojj.videoservice.bo.LoadCacheBo;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @Author @videopls.com
 * Created by  on 2018/12/4 下午6:59.
 * @Description:
 */
public class LoadCacheJob implements Job {


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        LoadCacheBo scheduleJob =(LoadCacheBo) jobExecutionContext.getMergedJobDataMap().get(
                "scheduleJob");

        scheduleJob.getLoadCacheTask().run();


    }
}
