package com.videojj.videoservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author //@videopls.com
 * Created by // on 2018/8/1 下午5:34.
 * @Description:
 */
@Getter
@Setter
@ToString
public class BasePageRequestDTO {

    private Integer currentPage;

    private Integer pageSize;

}
