package com.videojj.videoservice.bo;

import com.videojj.videoservice.dto.PageInfoDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/10 下午3:50.
 * @Description:
 */
@Getter
@Setter
@ToString
public class QueryUserParamBo extends PageInfoDTO {


    public QueryUserParamBo(int limitstart, Integer pageSize) {
        super(limitstart, pageSize);
    }


    private String username;


}
