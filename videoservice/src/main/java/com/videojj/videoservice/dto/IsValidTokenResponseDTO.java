package com.videojj.videoservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/29 上午11:25.
 * @Description:
 */
@Getter
@Setter
@ToString
public class IsValidTokenResponseDTO {

    private Boolean isValid;

    private String resCode;

    private String resMsg;

    private String attachInfo;

}
