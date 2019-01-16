package com.videojj.videoservice.dao;

import com.videojj.videoservice.entity.TbLaunchTaskExecute;
import com.videojj.videoservice.entity.TbLaunchTaskExecuteCriteria;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbLaunchTaskExecuteMapper {
    int countByParam(TbLaunchTaskExecuteCriteria param);

    int insertSelective(TbLaunchTaskExecute record);

    List<TbLaunchTaskExecute> selectByParam(TbLaunchTaskExecuteCriteria param);

    TbLaunchTaskExecute selectByPrimaryKey(Integer id);

    int updateByParamSelective(@Param("record") TbLaunchTaskExecute record, @Param("col") TbLaunchTaskExecuteCriteria param);

    int updateByPrimaryKeySelective(TbLaunchTaskExecute record);


}