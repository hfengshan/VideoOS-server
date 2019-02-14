package com.videojj.videopublicapi.controller;

import com.videojj.videocommon.constant.Constants;
import com.videojj.videoservice.apidto.CommonResponseDTO;
import com.videojj.videoservice.enums.UserBehaviorStatisticsEventTypeEnum;
import com.videojj.videoservice.enums.UserBehaviorStatisticsTypeEnum;
import com.videojj.videoservice.service.StatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by zhangzhewen on 2019/01/9 上午10:04.
 */
@Controller
@RequestMapping("/videoos-api")
public class StatisticsController extends AbstractController {

    @Resource
    private StatisticsService statisticsService;

    private static Logger log = LoggerFactory.getLogger(StatisticsController.class);

    @RequestMapping(value = "/statistic", method = RequestMethod.POST)
    public @ResponseBody
    CommonResponseDTO statistic(
            @RequestBody Map<String,Object> param
            ) {

        String videoId  = (String)param.get("videoId");
        Integer launchPlanId = Integer.parseInt((String)param.get("launchPlanId"));
        Integer type = Integer.parseInt((String)param.get("type"));
        Integer eventType = Integer.parseInt((String)param.get("eventType"));

        log.info("StatisticsController.statistic ==> videoId is {}, launchPlanId is {}, type is {}, eventType is {}", videoId,launchPlanId,type,eventType);

        CommonResponseDTO resDTO = new CommonResponseDTO();
        try {
            statisticsService.collectUserBehavior(videoId,launchPlanId, UserBehaviorStatisticsTypeEnum.getByValue(type), UserBehaviorStatisticsEventTypeEnum.getByValue(eventType));
        } catch (Exception e) {
            log.error("StatisticsController.statistic ==> occur error!", e);
            resDTO.setResCode(Constants.FAILCODE);
            resDTO.setResMsg(Constants.COMMONFAILMSG);
            return resDTO;
        }


        resDTO.setResCode(Constants.SUCESSCODE);
        resDTO.setResMsg(Constants.COMMONSUCCESSMSG);
        return resDTO;

    }


}
