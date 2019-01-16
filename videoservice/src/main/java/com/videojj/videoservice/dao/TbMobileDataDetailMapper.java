package com.videojj.videoservice.dao;

import com.videojj.videoservice.entity.TbMobileDataDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbMobileDataDetailMapper {

    int insertSelective(TbMobileDataDetail record);

    TbMobileDataDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TbMobileDataDetail record);


    void batchInsertDetail(@Param("detailList") List<TbMobileDataDetail> detailList);

    void deleteDataByMobileDataId(@Param("mobileDataId") Integer mobileDataId);
}