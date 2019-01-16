package com.videojj.videoservice.dao;

import com.github.pagehelper.Page;
import com.videojj.videoservice.entity.TbLaunchPlan;
import com.videojj.videoservice.entity.TbLaunchPlanApiInfoExt;
import com.videojj.videoservice.entity.TbLaunchPlanExt;
import com.videojj.videoservice.entity.TbLaunchPlanOperationExt;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TbLaunchPlanMapper extends Mapper<TbLaunchPlan> {

    Page<TbLaunchPlanExt> selectPage(@Param("interactionTypeId") Integer interactionTypeId);

    Page<TbLaunchPlanOperationExt> selectOperationPage(@Param("interactionTypeId") Integer interactionTypeId,@Param("launchTimeType")Byte launchTimeType);

    TbLaunchPlanExt selectDetailInfoById(@Param("launchPlanId") Integer launchPlanId);

    List<TbLaunchPlanApiInfoExt> selectByVideoId(@Param("id") Integer id, @Param("videoId") String videoId, @Param("nowDate") Date nowDate, @Param("launchTimeType") Byte launchTimeType);

    List<Byte> selectLaunchTimeTypeByVideoId(@Param("videoId") String videoId);

    List<String> selectCreativeFileUrlWithVideoTimeByVideoId(@Param("videoId") String videoId);

    List<String> selectCreativeFileUrlWithRealTimeByVideoId(@Param("videoId") String videoId);

    List<Map<String, Object>> selectCreativeFileUrlWithBjTimeByVideoId(@Param("videoId") String videoId);

    List<TbLaunchPlan> selectByOperationId(@Param("operationId")Integer operationId);

    List<TbLaunchPlan> selectBjTimeByDate(@Param("date")Date date);

    List<TbLaunchPlan> selectInUseByCreativeId(@Param("creativeId")Integer creativeId);

    List<TbLaunchPlan> selectOnlineByLaunchVideoId(@Param("launchVideoId")String launchVideoId);

    int updateStatusByOperationId(@Param("launchStatus")Byte launchStatus, @Param("launchPlanOperationId")Integer launchPlanOperationId);
}