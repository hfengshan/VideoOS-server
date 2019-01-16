package com.videojj.videoservice.handler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author zhangzhewen@videopls.com
 * Created by  on 2018/12/12 下午5:43.
 * @Description:
 */
public class LaunchPlanCloseJob implements Job {

    private static Logger log = LoggerFactory.getLogger(LaunchPlanCloseJob.class);

    @Override
    public void execute(JobExecutionContext context) {

        log.info("LaunchPlanCloseJob will run");
        try {

            LaunchPlanCloseService scheduleJob = (LaunchPlanCloseService) context.getMergedJobDataMap().get(
                    "scheduleJob");

            scheduleJob.run();

        } catch (Exception e) {

            log.info("LaunchPlanCloseJob run error!!" + e);
        }
        log.info("LaunchPlanCloseJob run success");
    }

}
