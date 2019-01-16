package com.videojj.videoservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @Author //@videopls.com
 * Created by // on 2018/8/14 下午3:42.
 * @Description:
 */
@Getter
@Setter
@ToString
public class RoleOwnAuthInfoResponseDTO {

    private String roleName;

    private Integer roleId;

    private List<Integer> nodeIdList;

    private String resCode;

    private String resMsg;

    private String attachInfo;
}
