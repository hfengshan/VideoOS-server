package com.videojj.videoservice.dao;


import com.videojj.videoservice.entity.TbTemplateZipFile;
import com.videojj.videoservice.entity.TbTemplateZipFileCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface TbTemplateZipFileMapper {
    int countByParam(TbTemplateZipFileCriteria param);

    int insertSelective(TbTemplateZipFile record);

    List<TbTemplateZipFile> selectByParam(TbTemplateZipFileCriteria param);

    TbTemplateZipFile selectByPrimaryKey(Integer id);

    int updateByParamSelective(@Param("record") TbTemplateZipFile record, @Param("col") TbTemplateZipFileCriteria param);

    int updateByPrimaryKeySelective(TbTemplateZipFile record);


}