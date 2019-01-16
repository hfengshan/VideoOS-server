package com.videojj.videoservice.task;

import com.videojj.videocommon.util.DateUtil;
import com.videojj.videoservice.entity.TbLaunchPlan;
import com.videojj.videoservice.entity.TbLaunchPlanCriteria;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/31 上午11:25.
 * @Description:
 */
@Service
public class LaunchPlanInitTask extends AbstractPlanTask{

    private static org.slf4j.Logger log = LoggerFactory.getLogger("LaunchPlanInitTask");


    @Override
    protected Date getDate() {

        Date today = DateUtil.getStartDate(new Date());

        return today;
    }

}
