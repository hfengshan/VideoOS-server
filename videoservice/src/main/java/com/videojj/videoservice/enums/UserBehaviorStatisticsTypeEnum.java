package com.videojj.videoservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserBehaviorStatisticsTypeEnum {
    INFO(1,"信息层"),HOTSPOT(2,"热点");

    private Integer value;
    private String name;

    public static UserBehaviorStatisticsTypeEnum getByValue(Integer value){
        if(values() != null && values().length > 0 && null != value){
            for(UserBehaviorStatisticsTypeEnum en : values()){
                if(en.value.equals(value)){
                    return en;
                }
            }
        }
        return null;
    }
}
