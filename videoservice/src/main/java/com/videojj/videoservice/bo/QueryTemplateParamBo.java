package com.videojj.videoservice.bo;

import com.videojj.videoservice.dto.PageInfoDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/8 下午2:36.
 * @Description:
 */
@Getter
@Setter
@ToString
public class QueryTemplateParamBo extends PageInfoDTO{


    public QueryTemplateParamBo(Integer limitstart, Integer pageSize) {
        super(limitstart, pageSize);
    }


    private Integer interactionTypeId;
}
