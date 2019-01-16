package com.videojj.videoservice.dto;

import com.videojj.videoservice.validation.annotation.IsNotInUseCreativeId;
import com.videojj.videoservice.validation.group.GroupSequence1;
import com.videojj.videoservice.validation.group.GroupSequence2;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/16 上午10:37.
 * @Description:
 */
@Getter
@Setter
@ToString
@GroupSequence({GroupSequence1.class, GroupSequence2.class, UpdateCreativeRequestDTO.class})
public class UpdateCreativeRequestDTO {

    @NotBlank(message = "{com.videojj.validation.NotBlank.creativeName.message}")
    private String creativeName;

    @NotBlank(message = "{com.videojj.validation.NotBlank.creativeContent.message}")
    private String creativeContent;

    @NotNull(message = "{com.videojj.validation.NotNull.interactionTemplateId.message}")
    private Integer interactionTemplateId;

    @NotNull(message = "{com.videojj.validation.NotNull.creativeId.message}",groups = GroupSequence1.class)
    @IsNotInUseCreativeId(groups = GroupSequence2.class)
    private Integer creativeId;

    @NotBlank(message = "{com.videojj.validation.NotBlank.interactionTemplateName.message}")
    private String interactionTemplateName;

    private Integer interactionTypeId;

    private String interactionTypeName;
}
