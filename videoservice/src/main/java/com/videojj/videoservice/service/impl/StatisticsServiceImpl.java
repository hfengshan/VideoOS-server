package com.videojj.videoservice.service.impl;

import com.videojj.videocommon.util.DateUtil;
import com.videojj.videoservice.dao.TbLaunchPlanMapper;
import com.videojj.videoservice.dto.UserBehaviorStatisticsDTO;
import com.videojj.videoservice.entity.TbLaunchPlan;
import com.videojj.videoservice.enums.UserBehaviorStatisticsEventTypeEnum;
import com.videojj.videoservice.enums.UserBehaviorStatisticsTypeEnum;
import com.videojj.videoservice.service.StatisticsService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 统计描述
 *
 * @author zhangzhewen
 * @date 2019/1/10
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {

    private static Logger log = LoggerFactory.getLogger(StatisticsServiceImpl.class);

    @Resource
    private JedisPool jedisPool;

    @Resource
    private TbLaunchPlanMapper tbLaunchPlanMapper;

    private static final String REDIS_PREFIX = "VIDEOOS_STATISTICS_USER_BEHAVIOR";
    private static final String REDIS_COUNT_PREFIX = "VIDEOOS_STATISTICS_VIDEO_PLAY_TIMES_COUNT";
    private static final String REDIS_SEPARATOR = "-----";
    private static final String TYPE_SEPARATOR = "-";
    private static final String REDIS_FUZZY_QUERY_SYMBOL = "*";



    @Override
    public UserBehaviorStatisticsDTO selectUserBehavior(String startDate, String endDate, String videoId, Integer interactionId) {
        Assert.notNull(startDate, "startDate不能为null");
        Assert.notNull(endDate, "endDate不能为null");

        UserBehaviorStatisticsDTO userBehaviorStatisticsDTO = new UserBehaviorStatisticsDTO();

        String videoIdCondition = REDIS_FUZZY_QUERY_SYMBOL;

        //视频播放次数
        int showExposureCount = 0;
        //信息层曝光量
        int infoShowExposureCount = 0;
        //信息层上的点击位曝光量
        int infoClickExposureCount = 0;
        //信息层上的点击位点击量
        int infoClickEventCount = 0;
        //热点曝光量
        int hotspotShowExposureCount = 0;
        //热点点击位曝光量
        int hotspotClickExposureCount = 0;
        //热点点击位点击量
        int hotspotClickEventCount = 0;

        if (StringUtils.isNotEmpty(videoId)) {
            videoIdCondition = videoId;
        }

        List<Integer> launchPlanIdListCondition = new ArrayList<>();
        boolean isNeedToHandle = true;
        if (interactionId != null) {
            TbLaunchPlan tbLaunchPlan = new TbLaunchPlan();
            tbLaunchPlan.setInteractionId(interactionId);
            launchPlanIdListCondition =
                    tbLaunchPlanMapper.select(tbLaunchPlan).stream().map(o -> o.getId()).collect(Collectors.toList());
            if(launchPlanIdListCondition.size()==0){
                isNeedToHandle = false;
            }
        }

        if(isNeedToHandle) {
            Jedis jedis = jedisPool.getResource();
            Pipeline pipeline = jedis.pipelined();
            try {
                HashMap<String, List<Response<Set<String>>>> typeAndRedisKeysMap = new HashMap<String, List<Response<Set<String>>>>() {{
                    put(UserBehaviorStatisticsTypeEnum.INFO.getValue().toString() + TYPE_SEPARATOR + UserBehaviorStatisticsEventTypeEnum.SHOW_EXPOSURE.getValue().toString(), new ArrayList<>());
                    put(UserBehaviorStatisticsTypeEnum.INFO.getValue().toString() + TYPE_SEPARATOR + UserBehaviorStatisticsEventTypeEnum.CLICK_EXPOSURE.getValue().toString(), new ArrayList<>());
                    put(UserBehaviorStatisticsTypeEnum.INFO.getValue().toString() + TYPE_SEPARATOR + UserBehaviorStatisticsEventTypeEnum.CLICK_EVENT.getValue().toString(), new ArrayList<>());
                    put(UserBehaviorStatisticsTypeEnum.HOTSPOT.getValue().toString() + TYPE_SEPARATOR + UserBehaviorStatisticsEventTypeEnum.SHOW_EXPOSURE.getValue().toString(), new ArrayList<>());
                    put(UserBehaviorStatisticsTypeEnum.HOTSPOT.getValue().toString() + TYPE_SEPARATOR + UserBehaviorStatisticsEventTypeEnum.CLICK_EXPOSURE.getValue().toString(), new ArrayList<>());
                    put(UserBehaviorStatisticsTypeEnum.HOTSPOT.getValue().toString() + TYPE_SEPARATOR + UserBehaviorStatisticsEventTypeEnum.CLICK_EVENT.getValue().toString(), new ArrayList<>());
                }};
                HashMap<String, List<Response<String>>> typeAndRedisValueMap = new HashMap<String, List<Response<String>>>() {{
                    put(UserBehaviorStatisticsTypeEnum.INFO.getValue().toString() + TYPE_SEPARATOR + UserBehaviorStatisticsEventTypeEnum.SHOW_EXPOSURE.getValue().toString(), new ArrayList<>());
                    put(UserBehaviorStatisticsTypeEnum.INFO.getValue().toString() + TYPE_SEPARATOR + UserBehaviorStatisticsEventTypeEnum.CLICK_EXPOSURE.getValue().toString(), new ArrayList<>());
                    put(UserBehaviorStatisticsTypeEnum.INFO.getValue().toString() + TYPE_SEPARATOR + UserBehaviorStatisticsEventTypeEnum.CLICK_EVENT.getValue().toString(), new ArrayList<>());
                    put(UserBehaviorStatisticsTypeEnum.HOTSPOT.getValue().toString() + TYPE_SEPARATOR + UserBehaviorStatisticsEventTypeEnum.SHOW_EXPOSURE.getValue().toString(), new ArrayList<>());
                    put(UserBehaviorStatisticsTypeEnum.HOTSPOT.getValue().toString() + TYPE_SEPARATOR + UserBehaviorStatisticsEventTypeEnum.CLICK_EXPOSURE.getValue().toString(), new ArrayList<>());
                    put(UserBehaviorStatisticsTypeEnum.HOTSPOT.getValue().toString() + TYPE_SEPARATOR + UserBehaviorStatisticsEventTypeEnum.CLICK_EVENT.getValue().toString(), new ArrayList<>());
                }};
                for (String day : DateUtil.getYyyymmddSpan(startDate, endDate)) {
                    if (launchPlanIdListCondition.size() == 0) {
                        setRedisKeysWithPipleline(typeAndRedisKeysMap, pipeline, videoIdCondition, REDIS_FUZZY_QUERY_SYMBOL, day);
                    } else {
                        for (Integer launchPlanId : launchPlanIdListCondition) {
                            setRedisKeysWithPipleline(typeAndRedisKeysMap, pipeline, videoIdCondition, launchPlanId.toString(), day);
                        }
                    }
                }
                pipeline.sync();
                for (Map.Entry<String, List<Response<Set<String>>>> entry : typeAndRedisKeysMap.entrySet()) {
                    for (Response<Set<String>> res : entry.getValue()) {
                        for (String redisKey : res.get()) {
                            typeAndRedisValueMap.get(entry.getKey()).add(pipeline.get(redisKey));
                        }
                    }
                }
                pipeline.sync();
                for (Map.Entry<String, List<Response<String>>> entry : typeAndRedisValueMap.entrySet()) {
                    infoShowExposureCount += calcCountFromRedis(entry, UserBehaviorStatisticsTypeEnum.INFO.getValue().toString() + TYPE_SEPARATOR + UserBehaviorStatisticsEventTypeEnum.SHOW_EXPOSURE.getValue().toString());
                    infoClickExposureCount += calcCountFromRedis(entry, UserBehaviorStatisticsTypeEnum.INFO.getValue().toString() + TYPE_SEPARATOR + UserBehaviorStatisticsEventTypeEnum.CLICK_EXPOSURE.getValue().toString());
                    infoClickEventCount += calcCountFromRedis(entry, UserBehaviorStatisticsTypeEnum.INFO.getValue().toString() + TYPE_SEPARATOR + UserBehaviorStatisticsEventTypeEnum.CLICK_EVENT.getValue().toString());
                    hotspotShowExposureCount += calcCountFromRedis(entry, UserBehaviorStatisticsTypeEnum.HOTSPOT.getValue().toString() + TYPE_SEPARATOR + UserBehaviorStatisticsEventTypeEnum.SHOW_EXPOSURE.getValue().toString());
                    hotspotClickExposureCount += calcCountFromRedis(entry, UserBehaviorStatisticsTypeEnum.HOTSPOT.getValue().toString() + TYPE_SEPARATOR + UserBehaviorStatisticsEventTypeEnum.CLICK_EXPOSURE.getValue().toString());
                    hotspotClickEventCount += calcCountFromRedis(entry, UserBehaviorStatisticsTypeEnum.HOTSPOT.getValue().toString() + TYPE_SEPARATOR + UserBehaviorStatisticsEventTypeEnum.CLICK_EVENT.getValue().toString());
                }
            } finally {
                jedisPool.returnResource(jedis);
            }
        }

        //计算视频总播放次数
        Jedis jedis = jedisPool.getResource();
        Pipeline pipeline = jedis.pipelined();
        try {
            List<Response<Set<String>>> videoPlayTimesRedisKeyResponseList = new ArrayList<>();
            List<Response<String>> videoPlayTimesRedisValueResponseList = new ArrayList<>();
            if(REDIS_FUZZY_QUERY_SYMBOL.equals(videoIdCondition)){
                for (String day : DateUtil.getYyyymmddSpan(startDate, endDate)) {
                    videoPlayTimesRedisKeyResponseList.add(pipeline.keys(buildVideoPlayTimesRedisCountKey(REDIS_FUZZY_QUERY_SYMBOL,day)));
                }
                pipeline.sync();
                for (Response<Set<String>> set : videoPlayTimesRedisKeyResponseList){
                    for(String redisKey : set.get()){
                        videoPlayTimesRedisValueResponseList.add(pipeline.get(redisKey));
                    }
                }
            }else {
                for (String day : DateUtil.getYyyymmddSpan(startDate, endDate)) {
                    videoPlayTimesRedisValueResponseList.add(pipeline.get(buildVideoPlayTimesRedisCountKey(videoIdCondition,day)));
                }
            }
            pipeline.sync();
            for (Response<String> value : videoPlayTimesRedisValueResponseList){
                if(StringUtils.isNotEmpty(value.get())){
                    showExposureCount += Integer.parseInt(value.get());
                }
            }
        }finally{
            jedisPool.returnResource(jedis);
        }

        userBehaviorStatisticsDTO.setShowExposureCount(showExposureCount);
        userBehaviorStatisticsDTO.setInfoShowExposureCount(infoShowExposureCount);
        userBehaviorStatisticsDTO.setInfoClickExposureCount(infoClickExposureCount);
        userBehaviorStatisticsDTO.setInfoClickEventCount(infoClickEventCount);
        userBehaviorStatisticsDTO.setInfoClickRate(infoClickExposureCount==0?0:((int)(infoClickEventCount*100/infoClickExposureCount*1.0)));
        userBehaviorStatisticsDTO.setHotspotShowExposureCount(hotspotShowExposureCount);
        userBehaviorStatisticsDTO.setHotspotClickExposureCount(hotspotClickExposureCount);
        userBehaviorStatisticsDTO.setHotspotClickEventCount(hotspotClickEventCount);
        userBehaviorStatisticsDTO.setHotspotClickRate(hotspotClickExposureCount==0?0:((int)(hotspotClickEventCount*100/hotspotClickExposureCount*1.0)));
        return userBehaviorStatisticsDTO;
    }

    private void setRedisKeysWithPipleline(HashMap<String, List<Response<Set<String>>>> typeAndRedisKeysMap, Pipeline pipeline, String videoIdCondition, String launchPlanIdCondition, String day) {
        typeAndRedisKeysMap.get(UserBehaviorStatisticsTypeEnum.INFO.getValue().toString() + TYPE_SEPARATOR + UserBehaviorStatisticsEventTypeEnum.SHOW_EXPOSURE.getValue().toString()).add(
                pipeline.keys(buildUserBehaviorRedisKey(
                        videoIdCondition, launchPlanIdCondition, day,
                        UserBehaviorStatisticsTypeEnum.INFO.getValue().toString(),
                        UserBehaviorStatisticsEventTypeEnum.SHOW_EXPOSURE.getValue().toString()
                ))
        );
        typeAndRedisKeysMap.get(UserBehaviorStatisticsTypeEnum.INFO.getValue().toString() + TYPE_SEPARATOR + UserBehaviorStatisticsEventTypeEnum.CLICK_EXPOSURE.getValue().toString()).add(
                pipeline.keys(buildUserBehaviorRedisKey(
                        videoIdCondition, launchPlanIdCondition, day,
                        UserBehaviorStatisticsTypeEnum.INFO.getValue().toString(),
                        UserBehaviorStatisticsEventTypeEnum.CLICK_EXPOSURE.getValue().toString()
                ))
        );
        typeAndRedisKeysMap.get(UserBehaviorStatisticsTypeEnum.INFO.getValue().toString() + TYPE_SEPARATOR + UserBehaviorStatisticsEventTypeEnum.CLICK_EVENT.getValue().toString()).add(
                pipeline.keys(buildUserBehaviorRedisKey(
                        videoIdCondition, launchPlanIdCondition, day,
                        UserBehaviorStatisticsTypeEnum.INFO.getValue().toString(),
                        UserBehaviorStatisticsEventTypeEnum.CLICK_EVENT.getValue().toString()
                ))
        );
        typeAndRedisKeysMap.get(UserBehaviorStatisticsTypeEnum.HOTSPOT.getValue().toString() + TYPE_SEPARATOR + UserBehaviorStatisticsEventTypeEnum.SHOW_EXPOSURE.getValue().toString()).add(
                pipeline.keys(buildUserBehaviorRedisKey(
                        videoIdCondition, launchPlanIdCondition, day,
                        UserBehaviorStatisticsTypeEnum.HOTSPOT.getValue().toString(),
                        UserBehaviorStatisticsEventTypeEnum.SHOW_EXPOSURE.getValue().toString()
                ))
        );
        typeAndRedisKeysMap.get(UserBehaviorStatisticsTypeEnum.HOTSPOT.getValue().toString() + TYPE_SEPARATOR + UserBehaviorStatisticsEventTypeEnum.CLICK_EXPOSURE.getValue().toString()).add(
                pipeline.keys(buildUserBehaviorRedisKey(
                        videoIdCondition, launchPlanIdCondition, day,
                        UserBehaviorStatisticsTypeEnum.HOTSPOT.getValue().toString(),
                        UserBehaviorStatisticsEventTypeEnum.CLICK_EXPOSURE.getValue().toString()
                ))
        );
        typeAndRedisKeysMap.get(UserBehaviorStatisticsTypeEnum.HOTSPOT.getValue().toString() + TYPE_SEPARATOR + UserBehaviorStatisticsEventTypeEnum.CLICK_EVENT.getValue().toString()).add(
                pipeline.keys(buildUserBehaviorRedisKey(
                        videoIdCondition, launchPlanIdCondition, day,
                        UserBehaviorStatisticsTypeEnum.HOTSPOT.getValue().toString(),
                        UserBehaviorStatisticsEventTypeEnum.CLICK_EVENT.getValue().toString()
                ))
        );
    }

    private int calcCountFromRedis(Map.Entry<String, List<Response<String>>> entry, String typeKey) {
        int count = 0;
        if (typeKey.equals(entry.getKey())) {
            for (Response<String> value : entry.getValue()) {
                count += Integer.parseInt(value.get());
            }
        }
        return count;
    }

    private String buildUserBehaviorRedisKey(String videoId, String launchPlanId, String day, String userBehaviorStatisticsType, String userBehaviorStatisticsEventType) {
        videoId = videoId.trim();
        launchPlanId = launchPlanId.trim();
        day = day.trim();
        userBehaviorStatisticsType = userBehaviorStatisticsType.trim();
        userBehaviorStatisticsEventType = userBehaviorStatisticsEventType.trim();
        String redisKey = REDIS_PREFIX
                + REDIS_SEPARATOR + videoId
                + REDIS_SEPARATOR + launchPlanId
                + REDIS_SEPARATOR + day
                + REDIS_SEPARATOR + userBehaviorStatisticsType
                + REDIS_SEPARATOR + userBehaviorStatisticsEventType;
        return redisKey;
    }

    private String buildVideoPlayTimesRedisCountKey(String videoId, String day) {
        videoId = videoId.trim();
        day = day.trim();
        String redisCountKey = REDIS_COUNT_PREFIX
                + REDIS_SEPARATOR + videoId
                + REDIS_SEPARATOR + day;
        return redisCountKey;
    }

    @Override
    public void collectUserBehavior(String videoId, Integer launchPlanId, UserBehaviorStatisticsTypeEnum userBehaviorStatisticsTypeEnum, UserBehaviorStatisticsEventTypeEnum userBehaviorStatisticsEventTypeEnum) {
        Assert.notNull(videoId, "videoId不能为null");
        Assert.notNull(userBehaviorStatisticsTypeEnum, "userBehaviorStatisticsTypeEnum不能为null");
        Assert.notNull(userBehaviorStatisticsEventTypeEnum, "userBehaviorStatisticsEventTypeEnum不能为null");

        videoId = videoId.trim();
        String today = DateUtil.toShortDateString(new Date());
        String redisKey = buildUserBehaviorRedisKey(videoId, launchPlanId.toString(), today, userBehaviorStatisticsTypeEnum.getValue().toString(), userBehaviorStatisticsEventTypeEnum.getValue().toString());

        Jedis jedis = jedisPool.getResource();
        try {
            jedis.incr(redisKey);
            log.info("StatisticsServiceImpl.collectUserBehavior()  ----> {} incr 1 from redis.",redisKey);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    @Override
    public void collectVideoPlayTimes(String videoId, Long times) {
        Assert.notNull(videoId, "videoId不能为null");
        Assert.notNull(times, "times不能为null");

        videoId = videoId.trim();
        String today = DateUtil.toShortDateString(new Date());
        String redisKey = buildVideoPlayTimesRedisCountKey(videoId,today);

        Jedis jedis = jedisPool.getResource();
        try {
            jedis.incrBy(redisKey,times);
            log.info("StatisticsServiceImpl.collectVideoPlayTimes()  ----> {} incr {} from redis.",redisKey,times);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

}
