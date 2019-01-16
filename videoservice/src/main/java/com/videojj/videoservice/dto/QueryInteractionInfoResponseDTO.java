package com.videojj.videoservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author zhangzhewen@videopls.com
 * Created by zhangzhewen on 2019/1/7 下午7:04.
 * @Description:
 */
@Getter
@Setter
@ToString
public class QueryInteractionInfoResponseDTO {

    private String interactionInfo;

    private Integer hotspot;

    private String resMsg;

    private String attachInfo;

    private String resCode;
}
