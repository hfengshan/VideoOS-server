package com.videojj.videoservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author //@videopls.com
 * Created by // on 2018/8/7 下午8:07.
 * @Description:
 */
@Getter
@Setter
@ToString
public class AuditLaunchPlanRequestDTO {

    private String username;

    private Integer launchPlanId;

    private Byte auditStatus;

    private String remark;

}
