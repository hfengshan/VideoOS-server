package com.videojj.videoservice.dao;

import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @Author @videopls.com
 * Created by  on 2018/11/2 下午6:20.
 * @Description:
 */
public interface TbCommonMapper {

    public Integer queryCountBySql(@Param("sqlStr") String sqlStr);

}
