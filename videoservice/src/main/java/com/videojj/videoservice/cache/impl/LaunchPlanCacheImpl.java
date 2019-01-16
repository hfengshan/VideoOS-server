package com.videojj.videoservice.cache.impl;

import com.alibaba.fastjson.JSONArray;
import com.videojj.videocommon.exception.ServiceException;
import com.videojj.videocommon.util.DateUtil;
import com.videojj.videoservice.apidto.LaunchApiQueryInfoResponseDTO;
import com.videojj.videoservice.cache.LaunchPlanCache;
import com.videojj.videoservice.dao.TbLaunchPlanMapper;
import com.videojj.videoservice.entity.TbLaunchPlanApiInfoExt;
import com.videojj.videoservice.enums.LaunchTimeTypeEnum;
import com.videojj.videoservice.service.impl.ExchangeDataUtil;
import org.apache.commons.collections.CollectionUtils;
import org.crazycake.shiro.RedisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Author wangpeng@videopls.com
 * Created by wangpeng on 2018/12/3 下午3:27.
 * @Description:
 */
@Component
public class LaunchPlanCacheImpl implements LaunchPlanCache{

    @Resource
    private TbLaunchPlanMapper tbLaunchPlanMapper;

    private static Logger log = LoggerFactory.getLogger("LaunchPlanCacheImpl");

    @Resource
    private RedisManager redisManager;

    @Override
    @Cacheable(value = "plan", key = "#videoId")
    public List<LaunchApiQueryInfoResponseDTO.LaunchInfo> selectByVideoId(Integer id, String videoId, Date nowDate, Byte launchTimeType) {

        log.info("LaunchPlanCacheImpl.selectByVideoId ==> it will get data from database....videoid is {}",videoId);

//        String pre = DateUtil.toShortDateString(new Date());

        byte[] value = redisManager.get(videoId.getBytes());

        List<LaunchApiQueryInfoResponseDTO.LaunchInfo> launchInfoList = null;

        if(null != value && value.length>0){

            String resultStr = new String(value);

            List<TbLaunchPlanApiInfoExt> res = JSONArray.parseArray(resultStr,TbLaunchPlanApiInfoExt.class);

            try {

                launchInfoList = ExchangeDataUtil.convertResult(res);

            } catch (Exception e) {

                log.error("LaunchPlanCacheImpl.selectByVideoId ==> exchangeData error!!,{}",e);

                throw new ServiceException("数据转换报错");
            }

            return launchInfoList;
        }else {

            String realVideoId = videoId.substring(10,videoId.length());

            log.info("LaunchPlanCacheImpl.selectByVideoId ==> it will query from database!!videoId is {}",realVideoId);

            List<TbLaunchPlanApiInfoExt> resultList = tbLaunchPlanMapper.selectByVideoId(id, realVideoId, nowDate, launchTimeType);

            if(CollectionUtils.isNotEmpty(resultList)) {

                try {

                    log.info("LaunchPlanCacheImpl.selectByVideoId ==> it exist in database!!length is {}",resultList.size());

                    launchInfoList = ExchangeDataUtil.convertResult(resultList);

                } catch (Exception e) {

                    log.error("LaunchPlanCacheImpl.selectByVideoId ==> exchangeData error!!",e);

                    throw new ServiceException("转换数据报错了");
                }

                log.info("LaunchPlanCacheImpl.selectByVideoId ==> queryResult is {}",JSONArray.toJSONString(resultList));

                redisManager.set(videoId.getBytes(), JSONArray.toJSONString(resultList).getBytes(), 86400);
            }
            return launchInfoList;
        }
    }

    @Override
    @CacheEvict(value = "plan", key = "#videoId")
    public void remove(String videoId) {

        log.info("LaunchPlanCacheImpl.remove ==> remove from cache, videoId: {}", videoId);
    }

    //TODO
    /**如果是存在的，那么就获取出来，如果是不存在的，那么就更新*/
    @Override
//    @CachePut(value = "plan", key = "#videoId")
    public List<TbLaunchPlanApiInfoExt> updateRedis(String videoId) {

        String pre = DateUtil.toShortDateString(new Date());

        List<TbLaunchPlanApiInfoExt> resultList = tbLaunchPlanMapper.selectByVideoId(null, videoId, new Date(), LaunchTimeTypeEnum.VIDIO_TIME.getValue());

        if(CollectionUtils.isNotEmpty(resultList)) {

            redisManager.set(pre.concat(videoId).getBytes(), JSONArray.toJSONString(resultList).getBytes(), 86400);
        }

        return resultList;

    }

    @Override
    @CachePut(value = "plan", key = "#videoId")
    public List<TbLaunchPlanApiInfoExt> updateLocalCache(String videoId) {

        String realVideoId = videoId.substring(10,videoId.length());

        List<TbLaunchPlanApiInfoExt> resultList = tbLaunchPlanMapper.selectByVideoId(null, realVideoId, new Date(), LaunchTimeTypeEnum.VIDIO_TIME.getValue());

        return resultList;
    }

}
