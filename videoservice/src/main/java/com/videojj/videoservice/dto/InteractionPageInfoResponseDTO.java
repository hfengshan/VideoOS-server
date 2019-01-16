package com.videojj.videoservice.dto;

import com.videojj.videocommon.dto.base.BaseResponseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @Author //@videopls.com
 * Created by // on 2018/8/1 下午5:30.
 * @Description:
 */
@Getter
@Setter
@ToString
public class InteractionPageInfoResponseDTO extends BaseResponseDTO{


    private List<InnerInteractionInfo> interactionInfoList;

    private String resMsg;

    private String attachInfo;

    private String resCode;

    private Long totalRecord;

    private Integer totalPage;

    @Getter
    @Setter
    @ToString
    public static class InnerInteractionInfo{

        private String createDate;

        private String interactionTypeName;

        private Integer interactionTypeId;

    }

}
