package com.videojj.videoservice.apidto;

import com.alibaba.fastjson.JSONObject;
import com.videojj.videoservice.dto.MonitorLinkDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/24 下午2:08.
 * @Description:
 */
@Getter
@Setter
@ToString
public class LaunchApiQueryInfoResponseDTO {

    private Integer totalRecord;

    private String resMsg;

    private String attachInfo;

    private String resCode;

    private List<LaunchInfo> launchInfoList;

    @Getter
    @Setter
    @ToString
    public static class LaunchInfo{

        private String id;

        private String template;

        private Long videoStartTime;

        private Long videoEndTime;

        private String clockStartTime;

        private String clockEndTime;

        private Long duration;

        private JSONObject data;

        private Integer creativeId;

        private Integer sumHotspot;

        private Integer hotspotOrder;

        private String nameMapper;

        private List<MonitorLinkDTO> hotspotTrackLink;

        private List<MonitorLinkDTO> infoTrackLink;

        private Integer launchPlanId;

    }
}
