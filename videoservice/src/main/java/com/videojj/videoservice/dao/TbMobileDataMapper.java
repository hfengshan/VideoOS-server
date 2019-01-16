package com.videojj.videoservice.dao;

import com.videojj.videoservice.entity.TbMobileData;
import com.videojj.videoservice.entity.TbMobileDataDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbMobileDataMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TbMobileData record);

    int insertSelective(TbMobileData record);

    TbMobileData selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TbMobileData record);

    int updateByPrimaryKey(TbMobileData record);

    TbMobileData selectByUserIdAndCreativeId(@Param(value = "userId") String userId, @Param(value = "creativeId") Integer creativeId);

}