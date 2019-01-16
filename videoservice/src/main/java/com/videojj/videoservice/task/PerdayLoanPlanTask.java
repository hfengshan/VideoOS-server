package com.videojj.videoservice.task;

import com.videojj.videoservice.bo.TimingTaskBo;
import com.videojj.videoservice.handler.TimingJob;
import com.videojj.videoservice.handler.TimingService;
import com.videojj.videoservice.quartz.QuartzManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/31 下午5:19.
 * @Description:
 */
@Component
public class PerdayLoanPlanTask {

    @Value("${video.common.cron.loanPlan}")
    private String cronExc;

    @Autowired
    private TimingService timingService;

    public void run() {

        /**每天晚上11点半触发这个任务，然后把次日的所有的任务都加载进去*/

        TimingTaskBo timingTaskBo = new TimingTaskBo();

        timingTaskBo.setTimingService(timingService);

        QuartzManager.addJob(new Date().toString(), TimingJob.class,cronExc,timingTaskBo);
    }
}
