package com.videojj.videoservice.apidto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/24 上午11:09.
 * @Description:
 */
@Getter
@Setter
@ToString
public class LaunchInfoRequestDTO {

    public String videoId;

    public CommonParam commonParam;

}
