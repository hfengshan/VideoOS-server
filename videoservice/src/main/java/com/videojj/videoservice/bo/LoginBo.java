package com.videojj.videoservice.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @Author @videopls.com
 * Created by  on 2018/9/13 下午6:51.
 * @Description:
 */
@Getter
@Setter
@ToString
public class LoginBo {

    private List<Integer> authList;

    private Integer roleId;

    private String roleName;


}
