package com.videojj.videoservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/8 下午4:34.
 * @Description:
 */
@Getter
@Setter
@ToString
public class AddCreativeRequestDTO {

    @NotBlank(message = "{com.videojj.validation.NotBlank.creativeName.message}")
    private String creativeName;

    @NotBlank(message = "{com.videojj.validation.NotBlank.creativeContent.message}")
    private String creativeContent;

    private String username;

    @NotNull(message = "{com.videojj.validation.NotNull.interactionTypeId.message}")
    private Integer interactionTypeId;

//    @NotBlank(message = "{com.videojj.validation.NotBlank.interactionTypeName.message}")
    private String interactionTypeName;

    @NotNull(message = "{com.videojj.validation.NotNull.interactionTemplateId.message}")
    private Integer interactionTemplateId;

    @NotBlank(message = "{com.videojj.validation.NotBlank.interactionTemplateName.message}")
    private String interactionTemplateName;

//    @NotEmpty(message = "{com.videojj.validation.NotEmpty.creativeIdList.message}")
    private List<Integer> creativeIdList;

}
