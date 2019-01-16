package com.videojj.videoservice.dto;

import com.videojj.videoservice.validation.annotation.IsNotInUseCreativeUrl;
import com.videojj.videoservice.validation.group.GroupSequence1;
import com.videojj.videoservice.validation.group.GroupSequence2;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;

/**
 * @Author //@videopls.com
 * Created by // on 2018/8/16 上午11:02.
 * @Description:
 */
@Getter
@Setter
@ToString
@GroupSequence({GroupSequence1.class,GroupSequence2.class,DeleteFileRequest.class})
public class DeleteFileRequest {

    @NotBlank(message = "{com.videojj.validation.NotBlank.fileUrl.message}",groups = GroupSequence1.class)
    @IsNotInUseCreativeUrl(groups = GroupSequence2.class)
    private String fileUrl;
}
