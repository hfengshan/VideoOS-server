package com.videojj.videoservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @Author //@videopls.com
 * Created by // on 2018/8/15 下午5:47.
 * @Description:
 */
@Getter
@Setter
@ToString
public class AllRoleInfoResponseDTO {

    private String resCode;

    private String resMsg;

    private String attachInfo;

    private List<RoleInfo> roleInfoList;
    @Getter
    @Setter
    @ToString
    public static class RoleInfo{

        private String roleName;

        private Integer roleId;

        private Boolean isSuperRole = false;
    }
}
