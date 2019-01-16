package com.videojj.videoservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * 投放请求dto
 *
 * @author zhangzhewen
 * @date 2018/12/24
 */
@Getter
@Setter
@ToString
public class LaunchLaunchPlanRequestDTO {

    @NotNull(message = "{com.videojj.validation.NotNull.launchPlanOperationId.message}")
    private Integer launchPlanId;

}
