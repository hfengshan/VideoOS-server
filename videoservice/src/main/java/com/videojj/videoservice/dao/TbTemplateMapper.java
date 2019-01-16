package com.videojj.videoservice.dao;


import com.videojj.videoservice.bo.QueryTemplateParamBo;
import com.videojj.videoservice.bo.TemplateSimpleInfoBo;
import com.videojj.videoservice.entity.TbTemplate;
import com.videojj.videoservice.entity.TbTemplateCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface TbTemplateMapper {
    int countByParam(TbTemplateCriteria param);

    int insertSelective(TbTemplate record);

    List<TbTemplate> selectByParam(TbTemplateCriteria param);

    TbTemplate selectByPrimaryKey(Integer id);

    int updateByParamSelective(@Param("record") TbTemplate record, @Param("col") TbTemplateCriteria param);

    int updateByPrimaryKeySelective(TbTemplate record);

    List<TbTemplate> selectByParamWithPage(@Param("param") QueryTemplateParamBo paramBo);

    TemplateSimpleInfoBo selectByTemplateId(@Param("templateId")Integer templateId);

    int logicallyDeleteByInteractionTypeName(@Param("interactionTypeName")String interactionTypeName, @Param("username")String username);

    int updateInteractionTypeNameByInteractionId(@Param("interactionId")Integer interactionId);
}