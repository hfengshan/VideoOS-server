package com.videojj.videoservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @Author //@videopls.com
 * Created by // on 2018/8/7 下午2:11.
 * @Description:
 */
@Getter
@Setter
@ToString
public class TemplatePageInfoResponseDTO {

    private String resMsg;

    private String attachInfo;

    private Integer totalRecord;

    private Integer totalPage;

    private List<TemplateInfo> templateInfoList;

    private String resCode;

    @Getter
    @Setter
    @ToString
    public static class TemplateInfo{

        private String interactionTypeName;

        private String createDate;

        private String templateName;

        private Integer interactionTypeId;

        private Integer templateId;
    }

}
