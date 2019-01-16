package com.videojj.videoservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @Author //@videopls.com
 * Created by // on 2018/8/14 下午2:47.
 * @Description:
 */
@Getter
@Setter
@ToString
public class UpdateRoleRequestDTO {

    private Integer roleId;

    private String roleName;

    private List<Integer> nodeIdList;
}
