package com.videojj.videoservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author //@videopls.com
 * Created by // on 2018/8/16 下午2:29.
 * @Description:
 */
@Getter
@Setter
@ToString
public class CreativeDetailInfoResponseDTO {

    private String resMsg;

    private String attachInfo;

    private String creativeName;

    private Integer creativeId;

    private Integer interactionId;

    private String interactionName;

    private Integer templateId;

    private String templateName;

    private String creativeContent;

    private String resCode;

    private Integer hotSpotNum;
}
