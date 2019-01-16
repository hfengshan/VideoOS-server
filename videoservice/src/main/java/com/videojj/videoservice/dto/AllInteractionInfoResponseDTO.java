package com.videojj.videoservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @Author //@videopls.com
 * Created by // on 2018/8/15 下午5:02.
 * @Description:
 */
@Getter
@Setter
@ToString
public class AllInteractionInfoResponseDTO {

    private String resCode;

    private String resMsg;

    private String attachInfo;

    private List<InteractionInfo> interactionInfoList;

    @Getter
    @Setter
    @ToString
    public static class InteractionInfo{

        private String interactionTypeName;

        private Integer interactionId;

        private String imgUrl;

        private String isSystem;
    }
}
