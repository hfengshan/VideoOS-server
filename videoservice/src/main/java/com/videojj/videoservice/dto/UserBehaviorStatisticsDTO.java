package com.videojj.videoservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户行为统计
 *
 * @author zhangzhewen
 * @date 2019/1/10
 */
@Getter
@Setter
@ToString
public class UserBehaviorStatisticsDTO {
    //视频播放次数
    private Integer showExposureCount;
    //信息层曝光量
    private Integer infoShowExposureCount;
    //信息层上的点击位曝光量
    private Integer infoClickExposureCount;
    //信息层上的点击位点击量
    private Integer infoClickEventCount;
    //热点曝光量
    private Integer hotspotShowExposureCount;
    //热点点击位曝光量
    private Integer hotspotClickExposureCount;
    //热点点击位点击量
    private Integer hotspotClickEventCount;

    private String resCode;
    private String resMsg;
    private String attachInfo;
}
