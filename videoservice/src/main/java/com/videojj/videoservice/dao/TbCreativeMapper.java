package com.videojj.videoservice.dao;

import com.github.pagehelper.Page;
import com.videojj.videoservice.dto.TbCreativeExtInfo;
import com.videojj.videoservice.entity.TbCreative;
import com.videojj.videoservice.entity.TbCreativeCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TbCreativeMapper {
    int insertSelective(TbCreative record);

    List<TbCreative> selectByParam(TbCreativeCriteria param);

    TbCreative selectByPrimaryKey(Integer id);

    int updateByParamSelective(@Param("record") TbCreative record, @Param("col") TbCreativeCriteria param);

    int updateByPrimaryKeySelective(TbCreative record);

    Page<TbCreative> selectPage(@Param("interactionid") Integer interactionid);

    TbCreative selectJoinInfoByPrimaryKey(@Param("creativeId") Integer creativeId);

    TbCreativeExtInfo selectJoinTemplateByName(@Param("creativeName") String creativeName);

    int logicallyDeleteByInteractionTypeName(@Param("interactionTypeName")String interactionTypeName, @Param("username")String username);

    int updateInteractionTypeNameByInteractionId(@Param("interactionId")Integer interactionId);

    int logicallyDeleteByTemplateId(@Param("templateId")Integer templateId);

    int updateTemplateNameByTemplateId(@Param("templateId")Integer templateId);
}