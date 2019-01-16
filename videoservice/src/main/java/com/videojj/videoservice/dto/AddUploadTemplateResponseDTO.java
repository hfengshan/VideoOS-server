package com.videojj.videoservice.dto;

import com.videojj.videocommon.dto.base.BaseResponseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author //@videopls.com
 * Created by // on 2018/8/6 下午3:21.
 * @Description:
 */
@Getter
@Setter
@ToString
public class AddUploadTemplateResponseDTO extends BaseResponseDTO {

    private String fileName;

    private String oldVersion;

    private String compressFileName;

}
