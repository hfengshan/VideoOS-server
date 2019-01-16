package com.videojj.videoservice.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.videojj.videocommon.util.DateUtil;
import com.videojj.videoservice.entity.TbLaunchPlan;
import com.videojj.videoservice.task.HandleTimeAndSendEmqHelper;
import com.videojj.videoservice.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author @videopls.com
 * Created by  on 2018/9/3 上午11:49.
 * @Description:
 */
@Service
public class QuartzServiceHelper {

    @Autowired
    private HandleTimeAndSendEmqHelper helper;

    private static Logger log = LoggerFactory.getLogger("QuartzServiceHelper");

    public void addQuartz(Date startDate, Date endDate, String launchTime, TbLaunchPlan param) {

        /**获取当前日期*/
        Date today = DateUtil.getStartDate(new Date());

        /**日期区间包含了今天*/
        if(DateUtil.compare(startDate,today)<=0
                &&DateUtil.compare(endDate,today)>=0){
            // 如果在时间区间内，那么就拆分一下投放时间，投放时间的格式已经不是逗号分割的了
//            List<String> timeList = Arrays.asList(launchTime.split(","));

            List<String> timeList = JsonUtil.parseLaunchTimeString(launchTime);

            log.info("QuartzServiceHelper.addQuartz ==> videoId is {}",param.getLaunchVideoId());

            helper.handle(today,timeList,param);

        }

    }


    public void removeQuartz(Date launchDateStart, Date launchDateEnd, String launchTime, TbLaunchPlan tbLaunchPlan) {

        /**获取当前日期*/
        Date today = DateUtil.getStartDate(new Date());

        /**日期区间包含了今天*/
        if (DateUtil.compare(launchDateStart, today) <= 0
                && DateUtil.compare(launchDateEnd, today) >= 0) {
            //TODO 移除的时候，时间拆分也要该
            List<String> timeList = JsonUtil.parseLaunchTimeString(launchTime);

            helper.stopPlan(today,timeList,tbLaunchPlan);

        }

    }
}
