package com.videojj.videoservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author //@videopls.com
 * Created by // on 2018/8/7 下午3:00.
 * @Description:
 */
@Getter
@Setter
@ToString
public class TemplateDeleteRequestDTO {

    private Integer interactionTemplateId;

    private String username;
}
