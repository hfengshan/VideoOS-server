package com.videojj.videoservice.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author //@videopls.com
 * Created by // on 2018/8/16 下午3:26.
 * @Description:
 */
@Getter
@Setter
@ToString
public class TemplateSimpleInfoBo {

    private Integer templateId;

    private String templateName;

    private Integer interactionId;

    private String interactionName;

    private String templateFileSourceName;
}
