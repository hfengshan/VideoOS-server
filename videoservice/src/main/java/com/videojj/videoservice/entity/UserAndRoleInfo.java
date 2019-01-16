package com.videojj.videoservice.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author @videopls.com
 * Created by  on 2018/9/13 下午6:55.
 * @Description:
 */
@Getter
@Setter
@ToString
public class UserAndRoleInfo {

    private Integer roleId;

    private String roleName;

    private String auths;

}
