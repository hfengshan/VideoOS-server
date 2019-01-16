package com.videojj.videoservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author @videopls.com
 * Created by  on 2018/9/19 上午10:25.
 * @Description:
 */
@Getter
@Setter
@ToString
public class UpdateUserPasswordRequestDTO {

    private String username;

    private String password;
}
