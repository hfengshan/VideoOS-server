package com.videojj.videoservice.dao;

import com.videojj.videoservice.entity.TbCreativeFile;
import com.videojj.videoservice.entity.TbCreativeFileCriteria;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbCreativeFileMapper {
    int countByCriteria(TbCreativeFileCriteria param);

    int insertSelective(TbCreativeFile record);

    List<TbCreativeFile> selectByCriteria(TbCreativeFileCriteria param);

    TbCreativeFile selectByPrimaryKey(Integer id);

    int updateByCriteriaSelective(@Param("record") TbCreativeFile record, @Param("col") TbCreativeFileCriteria param);

    int updateByPrimaryKeySelective(TbCreativeFile record);

}