package com.videojj.videoservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author //@videopls.com
 * Created by // on 2018/8/16 下午3:22.
 * @Description:
 */
@Getter
@Setter
@ToString
public class TemplateDetailInfoResponseDTO {

    private String interactionTypeName;

    private Integer templateId;

    private Integer interactionTypeId;

    private String templateName;

    private String templateFileSourceName;

    private String resCode;

    private String resMsg;

    private String attachInfo;
}
