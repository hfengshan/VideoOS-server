package com.videojj.videoportal.controller;

import com.videojj.videocommon.constant.Constants;
import com.videojj.videoservice.annotation.PermissionService;
import com.videojj.videoservice.dto.UserBehaviorStatisticsDTO;
import com.videojj.videoservice.service.StatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/10 下午1:40.
 * @Description:
 */

@Controller
public class StatisticsController {

    @Resource
    private StatisticsService statisticsService;

    private static Logger log = LoggerFactory.getLogger(StatisticsController.class);

    @PermissionService
    @RequestMapping(value = "/videoos/statistics/selectUserBehavior", method = RequestMethod.GET)
    public @ResponseBody
    UserBehaviorStatisticsDTO selectUserBehavior(@RequestParam String startDate,
                                                           @RequestParam String endDate,
                                                           @RequestParam(required = false) String videoId,
                                                           @RequestParam(required = false) Integer interactionId
    ) {
        UserBehaviorStatisticsDTO userBehaviorStatisticsDTO = new UserBehaviorStatisticsDTO();
        try{
            userBehaviorStatisticsDTO = statisticsService.selectUserBehavior(startDate,endDate,videoId,interactionId);
            userBehaviorStatisticsDTO.setResCode(Constants.SUCESSCODE);
            userBehaviorStatisticsDTO.setResMsg(Constants.COMMONSUCCESSMSG);
        }catch(Exception e){
            log.error("StatisticsController.selectUserBehavior ===> occur error!",e);
            userBehaviorStatisticsDTO.setResCode(Constants.FAILCODE);
            userBehaviorStatisticsDTO.setResMsg(Constants.COMMONFAILMSG);
        }
        return userBehaviorStatisticsDTO;
    }

}
