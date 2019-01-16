package com.videojj.videoservice.dao;

import com.videojj.videoservice.entity.TbCreativeImage;
import com.videojj.videoservice.entity.TbCreativeImageCriteria;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbCreativeImageMapper {

    int countByCriteria(TbCreativeImageCriteria example);

    int insertSelective(TbCreativeImage record);

    List<TbCreativeImage> selectByCriteria(TbCreativeImageCriteria example);

    TbCreativeImage selectByPrimaryKey(Integer id);

    int updateByCriteriaSelective(@Param("record") TbCreativeImage record, @Param("col") TbCreativeImageCriteria example);

    int updateByPrimaryKeySelective(TbCreativeImage record);

}