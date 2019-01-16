package com.videojj.videoservice.dao;

import com.videojj.videoservice.entity.TbNode;
import com.videojj.videoservice.entity.TbNodeCriteria;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbNodeMapper {
    int countByParam(TbNodeCriteria param);

    int insertSelective(TbNode record);

    List<TbNode> selectByParam(TbNodeCriteria param);

    TbNode selectByPrimaryKey(Integer id);

    int updateByParamSelective(@Param("record") TbNode record, @Param("col") TbNodeCriteria param);

    int updateByPrimaryKeySelective(TbNode record);


}