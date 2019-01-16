package com.videojj.videoservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author //@videopls.com
 * Created by // on 2018/8/10 下午1:48.
 * @Description:
 */
@Getter
@Setter
@ToString
public class AddUserRequestDTO {

    private String username;

    private Integer roleId;

    private String password;

}
