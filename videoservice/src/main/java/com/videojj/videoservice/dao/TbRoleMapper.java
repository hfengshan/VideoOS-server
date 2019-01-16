package com.videojj.videoservice.dao;

import com.videojj.videoservice.dto.PageInfoDTO;
import com.videojj.videoservice.entity.TbInteraction;
import com.videojj.videoservice.entity.TbInteractionCriteria;
import com.videojj.videoservice.entity.TbRole;
import com.videojj.videoservice.entity.TbRoleCriteria;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbRoleMapper {
    int countByParam(TbRoleCriteria param);

    int insertSelective(TbRole record);

    List<TbRole> selectByParam(TbRoleCriteria param);

    TbRole selectByPrimaryKey(Integer id);

    int updateByParamSelective(@Param("record") TbRole record, @Param("col") TbRoleCriteria param);

    int updateByPrimaryKeySelective(TbRole record);

    TbRole selectRoleInfoByUsername(@Param("username")String username);

    List<TbRole> selectByParamWithPage(@Param("col") TbRoleCriteria param,@Param("pageInfo") PageInfoDTO paramBo);

}