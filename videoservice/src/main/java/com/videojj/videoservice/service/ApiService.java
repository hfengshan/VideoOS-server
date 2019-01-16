package com.videojj.videoservice.service;

import com.alibaba.fastjson.JSONObject;
import com.videojj.videoservice.apidto.LaunchApiQueryInfoResponseDTO;
import com.videojj.videoservice.entity.TbMobileData;

import java.util.List;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/22 上午10:09.
 * @Description:
 */
public interface ApiService {

    LaunchApiQueryInfoResponseDTO queryInfoByVideoId(String videoId);

    LaunchApiQueryInfoResponseDTO queryInfoByCreativeName(String creativeName);

    void mobileModify(String userId, Integer creativeId, String businessInfo, String extraInfo);

    TbMobileData mobileQuery(String userId, Integer creativeId, String extraInfo);

    JSONObject queryInfoByCondition(Integer creativeId, String businessInfo) throws Exception;

    List<String> preloadLaunchInfoByVideoId(String videoId);
}
