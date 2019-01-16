package com.videojj.videoservice.apidto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author @videopls.com
 * Created by  on 2018/9/12 下午3:01.
 * @Description:
 */
@Getter
@Setter
@ToString
public class ConfigResponseDTO {

    private EmqConfig emqConfig;

    private String resCode;

    private String resMsg;

    private String attachInfo;

    @Getter
    @Setter
    @ToString
    public static class EmqConfig{

        private String host;

        private String port;

        private String username;

        private String password;

    }

}
