package com.videojj.videoservice.apidto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author @videopls.com
 * Created by  on 2018/11/5 下午2:39.
 * @Description:
 */
@Getter
@Setter
@ToString
public class CommonQueryResponseDTO {

    private String resCode;

    private String resMsg;

    private String attachInfo;

    private String commonResult;


}
