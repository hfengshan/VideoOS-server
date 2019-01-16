package com.videojj.videoservice.dao;

import com.github.pagehelper.Page;
import com.videojj.videoservice.entity.TbInteraction;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.Map;

public interface TbInteractionMapper extends Mapper<TbInteraction> {

    Page<TbInteraction> selectPage();

    int logicallyDeleteByInteractionTypeName(@Param("interactionTypeName")String interactionTypeName, @Param("username")String username);

    Map<String, Object> selectContentAndHotspot(Integer creativeId);
}