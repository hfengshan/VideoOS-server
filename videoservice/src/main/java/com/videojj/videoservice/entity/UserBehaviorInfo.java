package com.videojj.videoservice.entity;

import com.videojj.videoservice.enums.UserBehaviorStatisticsEventTypeEnum;
import com.videojj.videoservice.enums.UserBehaviorStatisticsTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户行为信息
 *
 * @author zhangzhewen
 * @date 2019/1/31
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class UserBehaviorInfo {
    private Long time;
    private String videoId;
    private Integer launchPlanId;
    private UserBehaviorStatisticsTypeEnum type;
    private UserBehaviorStatisticsEventTypeEnum eventType;
}
