package com.videojj.videoservice.init;

import com.videojj.videoservice.task.LaunchPlanCloseTask;
import com.videojj.videoservice.task.LaunchPlanInitTask;
import com.videojj.videoservice.task.PerdayLoanPlanTask;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/30 下午8:28.
 * @Description:
 */
@Component
public class InitLiveVideoTask implements InitializingBean {

    @Autowired
    private LaunchPlanInitTask launchPlanTask;

    @Autowired
    private PerdayLoanPlanTask perdayLoanPlanTask;

    @Autowired
    private LaunchPlanCloseTask launchPlanCloseTask;

    @Override
    public void afterPropertiesSet() throws Exception {

        launchPlanTask.run();

        perdayLoanPlanTask.run();

        launchPlanCloseTask.run();

    }
}

