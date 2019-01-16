package com.videojj.videoservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/16 上午11:22.
 * @Description:
 */
@Getter
@Setter
@ToString
public class CreativePageInfoResponseDTO {

    private List<CreativeInfo> creativeInfoList;

    private String resCode;

    private String resMsg;

    private String attachInfo;

    private Long totalRecord;

    private Integer totalPage;

    @Getter
    @Setter
    @ToString
    public static class CreativeInfo{

        private String createDate;

        private String creativeName;

        private Byte creativeStatus;

        private Integer interactionId;

        private String interactionName;

        private Integer templateId;

        private String templateName;

        private Integer creativeId;

        private Integer HotSpotNum;

    }


}
