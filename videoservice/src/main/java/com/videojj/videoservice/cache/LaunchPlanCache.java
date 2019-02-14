package com.videojj.videoservice.cache;

import com.videojj.videoservice.apidto.LaunchApiQueryInfoResponseDTO;
import com.videojj.videoservice.entity.TbLaunchPlanApiInfoExt;

import java.util.Date;
import java.util.List;

/**
 * @Author wangpeng@videopls.com
 * Created by wangpeng on 2018/12/3 下午3:26.
 * @Description:
 */
public interface LaunchPlanCache {

    List<LaunchApiQueryInfoResponseDTO.LaunchInfo> selectByVideoId(Integer id, String videoId, Date nowDate, Byte launchTimeType);

    public void remove(String videoId);

    public void removeAll(boolean redis);

    public List<TbLaunchPlanApiInfoExt> updateRedis(String videoId);

    public List<TbLaunchPlanApiInfoExt> updateLocalCache(String videoId);
}
