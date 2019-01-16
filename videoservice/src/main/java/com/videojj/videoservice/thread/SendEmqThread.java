package com.videojj.videoservice.thread;

import com.videojj.videoservice.apidto.LaunchApiQueryInfoResponseDTO;
import com.videojj.videoservice.dao.TbLaunchPlanMapper;
import com.videojj.videoservice.emqttd.MqttService;
import com.videojj.videoservice.entity.TbLaunchPlan;
import com.videojj.videoservice.entity.TbLaunchPlanApiInfoExt;
import com.videojj.videoservice.enums.LaunchTimeTypeEnum;
import com.videojj.videoservice.service.impl.ExchangeDataUtil;
import com.videojj.videoservice.service.impl.QuartzServiceHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Author @videopls.com
 * Created by  on 2018/9/3 下午12:18.
 * @Description:
 * 没加线程同步，所以使用要注意
 */
public class SendEmqThread implements Runnable{

    private TbLaunchPlan param;

    private QuartzServiceHelper quartzServiceHelper ;

    private TbLaunchPlanMapper tbLaunchPlanMapper;

    private MqttService mqttService;

    private static Logger log = LoggerFactory.getLogger("SendEmqThread");

    public SendEmqThread(TbLaunchPlan param, QuartzServiceHelper quartzServiceHelper, TbLaunchPlanMapper tbLaunchPlanMapper, MqttService mqttService) {

        this.param = param;

        this.quartzServiceHelper = quartzServiceHelper;

        this.mqttService = mqttService;

        this.tbLaunchPlanMapper = tbLaunchPlanMapper;
    }

    public void run(){

        /**如果是即时的投放的，那么就要直接发送消息*/
        if(param.getLaunchTimeType().intValue() == 1) {

            log.info("SendEmqThread.run ==>real time to launch....videoId is {}",param.getLaunchVideoId());

            try {

                sendEmqqt(param.getId(),param.getLaunchVideoId());
            } catch (Exception e) {

                log.error("send msg error!!videoId is {}",param.getLaunchVideoId(),e);
            }

        }

        /**如果是北京时间的，那么就要判断投放时间里面有没有当天，如果有，那么就要启动一个监听器*/
        if(param.getLaunchTimeType().intValue() == 2) {

            log.info("SendEmqThread.run ==>BJ time to a task....videoId is {}",param.getLaunchVideoId());

            quartzServiceHelper.addQuartz(param.getLaunchDateStart(),param.getLaunchDateEnd(),
                    param.getLaunchTime(),param);

        }

    }

    private void sendEmqqt(Integer id, String launchVideoId) throws Exception{

        /**及时的不能发送多个，所以是正确的*/
        List<TbLaunchPlanApiInfoExt> launchPlanList = tbLaunchPlanMapper.selectByVideoId(id, launchVideoId, null, LaunchTimeTypeEnum.REAL_TIME.getValue());

        if (CollectionUtils.isEmpty(launchPlanList) || StringUtils.isEmpty(launchPlanList.get(0).getTemplateFileName())) {

            log.info("SendEmqThread.sendEmqqt ==> there is no plan exist... videoId is {}",launchVideoId);

            return;
        }

        List<LaunchApiQueryInfoResponseDTO.LaunchInfo> result = ExchangeDataUtil.convertRealTimeResult(launchPlanList,null,true);

        mqttService.push(launchVideoId, result.get(0));

        return ;
    }
}
