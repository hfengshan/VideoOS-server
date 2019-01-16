package com.videojj.videoservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @Author //@videopls.com
 * Created by // on 2018/8/10 下午2:54.
 * @Description:
 */
@Getter
@Setter
@ToString
public class UserPageInfoResponseDTO {

    private List<UserInfo> userInfoList;

    private String resCode;

    private String resMsg;

    private String attachInfo;

    private Integer totalRecord;

    private Integer totalPage;

    @Getter
    @Setter
    @ToString
    public static class UserInfo{

        private String createDate;

        private String userName;

        private Integer roleId;

        private String roleName;

        private Integer userId;

        private Boolean isSuperRole;
    }

}
