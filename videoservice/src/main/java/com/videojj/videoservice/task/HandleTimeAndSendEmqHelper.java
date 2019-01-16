package com.videojj.videoservice.task;

import com.videojj.videocommon.util.DateUtil;
import com.videojj.videoservice.bo.LaunchPlanTaskBo;
import com.videojj.videoservice.entity.TbLaunchPlan;
import com.videojj.videoservice.handler.SendEmqJob;
import com.videojj.videoservice.handler.SendEmqService;
import com.videojj.videoservice.quartz.QuartzManager;
import com.videojj.videoservice.util.CronUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author @videopls.com
 * Created by  on 2018/9/3 上午11:09.
 * @Description:
 */
@Service
public class HandleTimeAndSendEmqHelper {

    @Autowired
    private SendEmqService sendEmqService;

    private static org.slf4j.Logger log = LoggerFactory.getLogger("HandleTimeAndSendEmqHelper");

    public void handle(Date today, List<String> launchTimeList, TbLaunchPlan tbLaunchPlan) {

        String date = DateUtil.toShortDateString(today);

        for(String hourAndMinite:launchTimeList){

            StringBuffer dateBuffer = new StringBuffer(date);

            List<String> timeArr = Arrays.asList(hourAndMinite.split(":"));

            buildTime(timeArr,dateBuffer);

            Date destTime = null;
            try {

                destTime = DateUtil.parseStdDateString(dateBuffer.toString());
                /**如果投放计划时间小于当前时间，就是这个计划已经过期了，那么今天就不需要执行了*/
                if(DateUtil.compare(new Date(),destTime)>0){

                    continue;

                }

            } catch (ParseException e) {

                log.error("parse date error!!! plan id is {} ,dateStr is {}",tbLaunchPlan.getId(),dateBuffer.toString());

                continue;
            }
            /**组装数据*/
            LaunchPlanTaskBo launchPlanTaskBo = new LaunchPlanTaskBo();

            String cronStr = CronUtil.getCron(destTime);

            launchPlanTaskBo.setJobCron(cronStr);

            launchPlanTaskBo.setLaunchPlanId(tbLaunchPlan.getId());

            launchPlanTaskBo.setJobName(tbLaunchPlan.getId().toString().concat("-").concat(hourAndMinite).concat("-").concat(tbLaunchPlan.getLaunchLenTime()));

            launchPlanTaskBo.setSendEmqService(sendEmqService);

            log.info("HandleTimeAndSendEmqHelper.handle ==> it will addJob,topic is {}",tbLaunchPlan.getLaunchVideoId());
            /**启动定时器*/
            QuartzManager.addJob(launchPlanTaskBo.getJobName(),SendEmqJob.class,launchPlanTaskBo.getJobCron(),launchPlanTaskBo);
        }

    }

    private void buildTime(List<String> timeArr, StringBuffer dateBuffer) {

        if(timeArr.get(0).length()==1){

            dateBuffer.append(" 0").append(timeArr.get(0));
        }else{

            dateBuffer.append(" ").append(timeArr.get(0));
        }
        if(timeArr.get(1).length()==1){

            dateBuffer.append(":0").append(timeArr.get(1)).append(":00");
        }else{

            dateBuffer.append(":").append(timeArr.get(1)).append(":00");
        }

    }


    public void stopPlan(Date today, List<String> timeList, TbLaunchPlan tbLaunchPlan) {

        String date = DateUtil.toShortDateString(today);

        StringBuffer dateBuffer = new StringBuffer(date);

        for(String hourAndMinite:timeList) {

            List<String> timeArr = Arrays.asList(hourAndMinite.split(":"));

            buildTime(timeArr, dateBuffer);

            Date destTime = null;
            try {

                destTime = DateUtil.parseStdDateString(dateBuffer.toString());
                /**如果投放计划时间大于或者等于当前时间，就是这个计划还没有执行，那么就需要停掉*/
                if (DateUtil.compare(new Date(), destTime) <= 0) {

                    QuartzManager.removeJob(tbLaunchPlan.getId().toString().concat("-").concat(hourAndMinite));

                }

            } catch (ParseException e) {

                log.error("parse date error!!! plan id is {} ,dateStr is {}", tbLaunchPlan.getId(), dateBuffer.toString());

                continue;
            }
        }

    }
}
