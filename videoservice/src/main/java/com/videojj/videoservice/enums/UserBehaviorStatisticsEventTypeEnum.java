package com.videojj.videoservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserBehaviorStatisticsEventTypeEnum{
    SHOW_EXPOSURE(1,"展示曝光"),CLICK_EXPOSURE(2,"点击曝光"),CLICK_EVENT(3,"点击事件");

    private Integer value;
    private String name;

    public static UserBehaviorStatisticsEventTypeEnum getByValue(Integer value){
        if(values() != null && values().length > 0 && null != value){
            for(UserBehaviorStatisticsEventTypeEnum en : values()){
                if(en.value.equals(value)){
                    return en;
                }
            }
        }
        return null;
    }
}
