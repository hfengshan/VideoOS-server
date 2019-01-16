package com.videojj.videoservice.handler;

import com.videojj.videoservice.bo.TimingTaskBo;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/31 下午5:43.
 * @Description:
 */
public class TimingJob implements Job {

    private static Logger log = LoggerFactory.getLogger("TimingJob");
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        log.info("timing job will run");
        try {

            TimingTaskBo scheduleJob =(TimingTaskBo) context.getMergedJobDataMap().get(
                    "scheduleJob");

            scheduleJob.getTimingService().run();

        }catch (Exception e) {

            log.info("timing job run error!!"+e);
        }
        log.info("timing job run success");
    }

}
