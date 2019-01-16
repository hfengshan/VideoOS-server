package com.videojj.videoservice.dao;

import com.videojj.videoservice.entity.TbTemplateFile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbTemplateFileMapper {

    int insertSelective(TbTemplateFile record);

    TbTemplateFile selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TbTemplateFile record);

    List<TbTemplateFile> selectByFileNameList(@Param("uploadFileNameList") List<String> uploadFileNameList);

    void deleteByTemplateId(@Param("templateId") int templateId);
}