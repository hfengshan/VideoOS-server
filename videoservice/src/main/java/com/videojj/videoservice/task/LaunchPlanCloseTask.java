package com.videojj.videoservice.task;

import com.videojj.videoservice.handler.LaunchPlanCloseJob;
import com.videojj.videoservice.handler.LaunchPlanCloseService;
import com.videojj.videoservice.quartz.QuartzManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author zhangzhewen@videopls.com
 * Created by  on 2018/12/12 下午5:19.
 * @Description:
 */
@Component
public class LaunchPlanCloseTask {

    @Value("${video.common.cron.launchPlanClose}")
    private String cronExc;

    @Autowired
    private LaunchPlanCloseService launchPlanCloseService;

    public void run() {
        /**每天晚上凌晨1点，将前一天的所有的任务已经全部完成的北京时间投放的和视频时间投放的，状态更新为关闭,查询增加一个判断*/
        QuartzManager.addJob(this.getClass().getName() + " - " + new Date().toString(), LaunchPlanCloseJob.class, cronExc, launchPlanCloseService);
    }

}
