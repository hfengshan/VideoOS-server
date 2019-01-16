package com.videojj.videoservice.apidto;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author @videopls.com
 * Created by  on 2018/10/26 下午2:08.
 * @Description:
 */
@Getter
@Setter
@ToString
public class MobileQueryResponseDTO {

    private String resMsg;

    private String attachInfo;

    private String resCode;

    private JSONObject businessInfo;

    private String extraInfo;

}
