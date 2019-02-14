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
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

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

    @Resource
    private JedisPool jedisPool;

    private static final String REDIS_PREFIX = "LAUNCH_PLAN_CACHE_";

    @Override
    @Cacheable(value = "plan", key = "#videoId")
    public List<LaunchApiQueryInfoResponseDTO.LaunchInfo> selectByVideoId(Integer id, String videoId, Date nowDate, Byte launchTimeType) {

        log.info("LaunchPlanCacheImpl.selectByVideoId ==> it will get data from database....videoid is {}",videoId);

        String pre = DateUtil.toShortDateString(new Date());

        byte[] value = redisManager.get(REDIS_PREFIX.concat(pre).concat(videoId).getBytes());

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

            log.info("LaunchPlanCacheImpl.selectByVideoId ==> it will query from database!!videoId is {}",videoId);

            List<TbLaunchPlanApiInfoExt> resultList = tbLaunchPlanMapper.selectByVideoId(id, videoId, nowDate, launchTimeType);

            if(CollectionUtils.isNotEmpty(resultList)) {

                try {

                    log.info("LaunchPlanCacheImpl.selectByVideoId ==> it exist in database!!length is {}",resultList.size());

                    launchInfoList = ExchangeDataUtil.convertResult(resultList);

                } catch (Exception e) {

                    log.error("LaunchPlanCacheImpl.selectByVideoId ==> exchangeData error!!",e);

                    throw new ServiceException("转换数据报错了");
                }

                log.info("LaunchPlanCacheImpl.selectByVideoId ==> queryResult is {}",JSONArray.toJSONString(resultList));

                redisManager.set(REDIS_PREFIX.concat(pre).concat(videoId).getBytes(), JSONArray.toJSONString(resultList).getBytes(), 86400);
            }
            return launchInfoList;
        }
    }

    @Override
    @CacheEvict(value = "plan", key = "#videoId")
    public void remove(String videoId) {

        log.info("LaunchPlanCacheImpl.remove ==> remove from cache, videoId: {}", videoId);
        Jedis jedis = jedisPool.getResource();
        try{
            jedis.del(REDIS_PREFIX.concat(DateUtil.toShortDateString(new Date())).concat(videoId));
        }finally {
            jedisPool.returnResource(jedis);
        }

    }

    @Override
    @CacheEvict(value = "plan", allEntries = true)
    public void removeAll(boolean redis) {
        if(redis){
            //删除redis中的缓存
            Jedis jedis = jedisPool.getResource();
            Pipeline pipeline = jedis.pipelined();
            try{
                for(String key : jedis.keys(REDIS_PREFIX.concat(DateUtil.toShortDateString(new Date())).concat("*"))){
                    pipeline.del(key);
                }
                pipeline.sync();
            }finally {
                jedisPool.returnResource(jedis);
            }
        }
    }

    //TODO
    /**如果是存在的，那么就获取出来，如果是不存在的，那么就更新*/
    @Override
//    @CachePut(value = "plan", key = "#videoId")
    public List<TbLaunchPlanApiInfoExt> updateRedis(String videoId) {

        String pre = DateUtil.toShortDateString(new Date());

        List<TbLaunchPlanApiInfoExt> resultList = tbLaunchPlanMapper.selectByVideoId(null, videoId, new Date(), LaunchTimeTypeEnum.VIDIO_TIME.getValue());

        if(CollectionUtils.isNotEmpty(resultList)) {

            redisManager.set(REDIS_PREFIX.concat(pre).concat(videoId).getBytes(), JSONArray.toJSONString(resultList).getBytes(), 86400);
        }else{

            redisManager.del(REDIS_PREFIX.concat(pre).concat(videoId).getBytes());
        }

        return resultList;

    }

    @Override
    @CachePut(value = "plan", key = "#videoId")
    public List<TbLaunchPlanApiInfoExt> updateLocalCache(String videoId) {

        List<TbLaunchPlanApiInfoExt> resultList = tbLaunchPlanMapper.selectByVideoId(null, videoId, new Date(), LaunchTimeTypeEnum.VIDIO_TIME.getValue());

        return resultList;
    }

}
