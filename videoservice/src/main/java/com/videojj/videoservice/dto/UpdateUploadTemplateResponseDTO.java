package com.videojj.videoservice.dto;

import com.videojj.videocommon.dto.base.BaseResponseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author //@videopls.com
 * Created by // on 2018/8/7 上午11:21.
 * @Description:
 */
@Getter
@Setter
@ToString
public class UpdateUploadTemplateResponseDTO extends BaseResponseDTO{

    private String fileName;

    private String oldVersion;

    private String compressFileName;
}
