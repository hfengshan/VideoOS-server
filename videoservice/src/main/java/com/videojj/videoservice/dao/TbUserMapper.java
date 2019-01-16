package com.videojj.videoservice.dao;


import com.videojj.videoservice.bo.QueryUserParamBo;
import com.videojj.videoservice.entity.TbUser;
import com.videojj.videoservice.entity.TbUserCriteria;
import com.videojj.videoservice.entity.TbUserExt;
import com.videojj.videoservice.entity.UserAndRoleInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface TbUserMapper {
    
    int countByParam(TbUserCriteria param);

    int insertSelective(TbUser record);

    List<TbUser> selectByParam(TbUserCriteria param);

    TbUser selectByPrimaryKey(Integer id);

    int updateByParamSelective(@Param("record") TbUser record, @Param("col") TbUserCriteria param);

    int updateByPrimaryKeySelective(TbUser record);

    List<TbUserExt> selectByParamWithPage(@Param("param")QueryUserParamBo paramBo);

    UserAndRoleInfo selectRoleInfoByUsername(@Param("username") String username);
}