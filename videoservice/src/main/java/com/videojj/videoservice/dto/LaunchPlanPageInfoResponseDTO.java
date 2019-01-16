package com.videojj.videoservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/7 下午7:53.
 * @Description:
 */
@Getter
@Setter
@ToString
public class LaunchPlanPageInfoResponseDTO {

    private List<LaunchPlanInfo> launchPlanList;

    private String resCode;

    private String resMsg;

    private String attachInfo;

    private Long totalRecord;

    private Integer totalPage;

    @Getter
    @Setter
    @ToString
    public static class LaunchPlanInfo{

        private String  interactionTypeName;

        private String  createDate;

        private String  launchName;

        private String  creativeName;

        private Integer  creativeId;

        private Byte  launchStatus;

        private Integer launchPlanId;

        private Integer launchLenTime;

        private Byte launchTimeType;

        private String launchStartTime;

    }
}
