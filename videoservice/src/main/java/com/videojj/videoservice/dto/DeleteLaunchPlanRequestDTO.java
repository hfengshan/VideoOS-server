package com.videojj.videoservice.dto;

import lombok.*;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/7 下午8:12.
 * @Description:
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DeleteLaunchPlanRequestDTO {

    private Integer launchPlanId;

    private String username;

}
