package com.videojj.videoservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.management.relation.RoleInfo;
import java.util.List;

/**
 * @Author //@videopls.com
 * Created by // on 2018/8/14 下午2:59.
 * @Description:
 */
@Getter
@Setter
@ToString
public class RolePageInfoResponseDTO {

    private String resCode;

    private String resMsg;

    private String attachInfo;

    private Integer totalRecord;

    private Integer totalPage;

    private List<RoleInfo> roleInfoList;

    @Getter
    @Setter
    @ToString
    public static class RoleInfo{

        private Integer roleId;

        private String roleName;

        private String createDate;

        private String roleDesc;

        private Boolean isSuperRole = false;
    }
}
