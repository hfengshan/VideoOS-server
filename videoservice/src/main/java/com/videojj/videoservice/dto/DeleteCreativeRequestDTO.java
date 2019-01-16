package com.videojj.videoservice.dto;

import com.videojj.videoservice.validation.annotation.IsNotInUseCreativeId;
import com.videojj.videoservice.validation.group.GroupSequence1;
import com.videojj.videoservice.validation.group.GroupSequence2;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotNull;

/**
 * @Author //@videopls.com
 * Created by // on 2018/8/8 下午4:56.
 * @Description:
 */
@Getter
@Setter
@ToString
@GroupSequence({GroupSequence1.class, GroupSequence2.class, DeleteCreativeRequestDTO.class})
public class DeleteCreativeRequestDTO {

    @NotNull(message = "{com.videojj.validation.NotNull.creativeId.message}",groups = GroupSequence1.class)
    @IsNotInUseCreativeId(groups = GroupSequence2.class)
    private Integer creativeId;

    private String username;
}
