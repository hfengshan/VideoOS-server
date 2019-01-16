package com.videojj.videoservice.dto;

import com.videojj.videoservice.validation.annotation.IsNotInUseCreativeId;
import com.videojj.videoservice.validation.group.GroupSequence1;
import com.videojj.videoservice.validation.group.GroupSequence2;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@GroupSequence({GroupSequence1.class, GroupSequence2.class,UpdateUploadInteractionFileRequestDTO.class})
public class UpdateUploadInteractionFileRequestDTO {

    private MultipartFile file;

    private Integer creativeFileId;

    @NotNull(message = "{com.videojj.validation.NotNull.creativeId.message}",groups = GroupSequence1.class)
    @IsNotInUseCreativeId(groups = GroupSequence2.class)
    private Integer creativeId;
}
