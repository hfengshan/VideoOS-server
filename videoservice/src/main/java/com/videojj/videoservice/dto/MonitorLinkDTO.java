package com.videojj.videoservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 监控链接信息
 *
 * @author zhangzhewen
 * @date 2019/1/4
 */
@Getter
@Setter
@ToString
public class MonitorLinkDTO {

    private String exposureTrackLink;

    private String clickTrackLink;
}
