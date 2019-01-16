package com.videojj.videoservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author //@videopls.com
 * Created by // on 2018/8/1 下午6:47.
 * @Description:
 */
@Getter
@Setter
@ToString
public class PageInfoDTO {



    private Integer offset;

    private Integer size;

    public PageInfoDTO(Integer limitstart, Integer pageSize) {

        this.offset = limitstart;

        this.size = pageSize;
    }
}
