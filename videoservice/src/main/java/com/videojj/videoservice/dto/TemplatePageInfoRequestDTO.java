package com.videojj.videoservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author //@videopls.com
 * Created by // on 2018/8/7 下午2:11.
 * @Description:
 */
@Getter
@Setter
@ToString
public class TemplatePageInfoRequestDTO {

    private Integer interactionTypeId;

    private Integer currentPage;

    private Integer pageSize;
}
