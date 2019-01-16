package com.videojj.videoservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author //@videopls.com
 * Created by // on 2018/8/1 下午7:04.
 * @Description:
 */
@Getter
@Setter
@ToString
public class InteractionInfoResponseDTO {

    private String interactionTypeName;

    private Integer interactionTypeId;

    private String configInfo;

    private String fileName;

    private String resMsg;

    private String attachInfo;

    private String resCode;
}
