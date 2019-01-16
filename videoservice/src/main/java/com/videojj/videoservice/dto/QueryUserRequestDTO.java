package com.videojj.videoservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author //@videopls.com
 * Created by // on 2018/8/10 下午2:47.
 * @Description:
 */
@Getter
@Setter
@ToString
public class QueryUserRequestDTO {

    private Integer currentPage;

    private Integer pageSize;

    private String qryusername;

}
