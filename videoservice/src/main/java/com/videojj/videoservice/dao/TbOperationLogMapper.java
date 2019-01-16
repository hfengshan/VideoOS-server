package com.videojj.videoservice.dao;

import com.videojj.videoservice.dto.PageInfoDTO;
import com.videojj.videoservice.entity.TbOperationLog;
import com.videojj.videoservice.entity.TbOperationLogExt;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

public interface TbOperationLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TbOperationLog record);

    int insertSelective(TbOperationLog record);

    TbOperationLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TbOperationLog record);

    int updateByPrimaryKey(TbOperationLog record);

    List<TbOperationLogExt> selectByParamWithPage(@Param("pageInfo") PageInfoDTO pageInfo);

    int count();
}