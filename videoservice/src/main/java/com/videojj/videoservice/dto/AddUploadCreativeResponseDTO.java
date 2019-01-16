package com.videojj.videoservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author //@videopls.com
 * Created by // on 2018/8/15 下午2:46.
 * @Description:
 */
@Getter
@Setter
@ToString
public class AddUploadCreativeResponseDTO {

    private String resCode;

    private String resMsg;

    private String attachInfo;

    private String fileUrl;

    private Integer creativeFileId;

    private Integer width;

    private Integer height;

    private Integer duration;
}
