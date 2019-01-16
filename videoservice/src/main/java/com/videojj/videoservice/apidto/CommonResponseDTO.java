package com.videojj.videoservice.apidto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author @videopls.com
 * Created by zhangzhewen on 2018/10/25 下午2:08.
 * @Description:
 */
@Getter
@Setter
@ToString
public class CommonResponseDTO {

    private String resCode;

    private String resMsg;

    private String attachInfo;


}
