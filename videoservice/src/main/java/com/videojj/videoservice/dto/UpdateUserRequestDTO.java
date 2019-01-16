package com.videojj.videoservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author //@videopls.com
 * Created by // on 2018/8/10 下午2:28.
 * @Description:
 */
@Getter
@Setter
@ToString
public class UpdateUserRequestDTO {

    private String username;

    private Integer roleId;

    private String password;

    private Integer userId;

}
