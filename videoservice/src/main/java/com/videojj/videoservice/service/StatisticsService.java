package com.videojj.videoservice.service;

import com.videojj.videoservice.dto.UserBehaviorStatisticsDTO;
import com.videojj.videoservice.enums.UserBehaviorStatisticsEventTypeEnum;
import com.videojj.videoservice.enums.UserBehaviorStatisticsTypeEnum;

/**
 * 统计接口
 *
 * @author zhangzhewen
 * @date 2019/1/10
 */
public interface StatisticsService {
    void collectUserBehavior(String videoId, Integer launchPlanId, UserBehaviorStatisticsTypeEnum userBehaviorStatisticsTypeEnum, UserBehaviorStatisticsEventTypeEnum userBehaviorStatisticsEventTypeEnum);

    UserBehaviorStatisticsDTO selectUserBehavior(String startDate, String endDate, String videoId, Integer interactionId);
}
