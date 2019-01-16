package com.videojj.videoservice.dao;

import com.videojj.videoservice.entity.TbLaunchPlanOperation;
import com.videojj.videoservice.entity.TbLaunchPlanOperationExt;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TbLaunchPlanOperationMapper extends Mapper<TbLaunchPlanOperation> {

    TbLaunchPlanOperationExt selectDetailInfoById(@Param("id") Integer id);

    List<TbLaunchPlanOperation> selectFinishedLaunchPlanOperation();
}