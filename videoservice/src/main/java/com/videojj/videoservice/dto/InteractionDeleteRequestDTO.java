package com.videojj.videoservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/1 下午8:32.
 * @Description:
 */
@Getter
@Setter
@ToString
public class InteractionDeleteRequestDTO {

    private String interactionTypeName;

    private String username;
}
