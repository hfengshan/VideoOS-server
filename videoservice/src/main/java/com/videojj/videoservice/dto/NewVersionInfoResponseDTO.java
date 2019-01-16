package com.videojj.videoservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author //@videopls.com
 * Created by // on 2018/8/22 上午10:12.
 * @Description:
 */
@Getter
@Setter
@ToString
public class NewVersionInfoResponseDTO {

    private String resMsg;

    private String version;

    private String downloadUrl;

    private String resCode;
}
