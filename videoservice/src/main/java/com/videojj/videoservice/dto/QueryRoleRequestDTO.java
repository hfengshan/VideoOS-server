package com.videojj.videoservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/14 下午2:58.
 * @Description:
 */
@Getter
@Setter
@ToString
public class QueryRoleRequestDTO {

    private Integer currentPage;

    private Integer pageSize;

}
