package com.videojj.videoservice.handler;

import com.videojj.videoservice.bo.LaunchPlanTaskBo;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 定时任务运行（反射出来的类）
 * @Description
 * @author
 * 2016 下午2:39:37 ^_^
 */
@DisallowConcurrentExecution
public class SendEmqJob implements Job {

    private static Logger log = LoggerFactory.getLogger("SendEmqJob");
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        log.info("task will run");
        try {
            //ScheduleJob具体参数，
            LaunchPlanTaskBo scheduleJob =(LaunchPlanTaskBo) context.getMergedJobDataMap().get(
                    "scheduleJob");

            scheduleJob.getSendEmqService().exec(scheduleJob.getJobName(),
                    scheduleJob.getLaunchPlanId());

        }catch (Exception e) {
            log.info("执行任务报错了"+e);
        }
        log.info("task finished");
    }
}