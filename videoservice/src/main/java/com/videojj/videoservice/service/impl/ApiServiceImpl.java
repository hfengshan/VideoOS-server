package com.videojj.videoservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.videojj.videocommon.constant.Constants;
import com.videojj.videocommon.exception.ServiceException;
import com.videojj.videocommon.interpreter.json2sql.Json2SqlConverter;
import com.videojj.videocommon.util.DateUtil;
import com.videojj.videoservice.apidto.LaunchApiQueryInfoResponseDTO;
import com.videojj.videoservice.cache.LaunchPlanCache;
import com.videojj.videoservice.dao.*;
import com.videojj.videoservice.dto.TbCreativeExtInfo;
import com.videojj.videoservice.entity.TbMobileData;
import com.videojj.videoservice.entity.TbMobileDataDetail;
import com.videojj.videoservice.enums.LaunchTimeTypeEnum;
import com.videojj.videoservice.service.ApiService;
import com.videojj.videoservice.util.JsonUtil;
import org.crazycake.shiro.RedisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static com.videojj.videocommon.constant.Constants.PRELOAD_VIDEO_ID_REDIS_KEY_PREFIX;


/**

 * 移动端api接口的service实现类。

 * @author zhangzhewen
 * @Date    2019.1.14

 * @version 1.00

 */
@Service
public class ApiServiceImpl implements ApiService {

    @Resource
    private TbLaunchPlanMapper tbLaunchPlanMapper;

    @Resource
    private TbCreativeMapper tbCreativeMapper;

    @Resource
    private TbMobileDataMapper tbMobileDataMapper;

    private static Logger log = LoggerFactory.getLogger("ApiServiceImpl");

    @Resource
    private TbMobileDataDetailMapper tbMobileDataDetailMapper;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private TbCommonMapper tbCommonMapper;

    @Resource
    private LaunchPlanCache launchPlanCache;

    @Resource
    private RedisManager redisManager;

    /**只能点播使用*/
    @Override
    public LaunchApiQueryInfoResponseDTO queryInfoByVideoId(String videoId) {

        LaunchApiQueryInfoResponseDTO res = new LaunchApiQueryInfoResponseDTO();

        List<LaunchApiQueryInfoResponseDTO.LaunchInfo> launchInfoList = launchPlanCache.selectByVideoId(null,videoId, DateUtil.getStartDate(new Date()), LaunchTimeTypeEnum.VIDIO_TIME.getValue());

        res.setLaunchInfoList(launchInfoList);

        if(CollectionUtils.isEmpty(launchInfoList)){

            res.setTotalRecord(0);
        }else {

            res.setTotalRecord(launchInfoList.size());
        }

        return res;
    }

    @Override
    public LaunchApiQueryInfoResponseDTO queryInfoByCreativeName(String creativeName) {

        TbCreativeExtInfo creativeExtInfo = tbCreativeMapper.selectJoinTemplateByName(creativeName);

        if(null == creativeExtInfo){

            return null;
        }

        LaunchApiQueryInfoResponseDTO.LaunchInfo launchInfo = new LaunchApiQueryInfoResponseDTO.LaunchInfo();

        launchInfo.setTemplate(creativeExtInfo.getTemplateFileName());

        launchInfo.setDuration(30000L);

        launchInfo.setId(UUID.randomUUID().toString());

        launchInfo.setData(JSONObject.parseObject(creativeExtInfo.getMaterialContent()));

        LaunchApiQueryInfoResponseDTO response = new LaunchApiQueryInfoResponseDTO();

        List<LaunchApiQueryInfoResponseDTO.LaunchInfo> launchInfoList = new ArrayList<>();

        launchInfoList.add(launchInfo);

        response.setLaunchInfoList(launchInfoList);

        return response;
    }

    @Override
    public void mobileModify(String userId, Integer creativeId, String businessInfo, String extraInfo) {
        TbMobileData existingTbMobileData = tbMobileDataMapper.selectByUserIdAndCreativeId(userId, creativeId);

        transactionTemplate.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean doInTransaction(TransactionStatus transactionStatus) {

                try {

                    if(existingTbMobileData == null){
                        TbMobileData tbMobileData = TbMobileData.builder().userId(userId).creativeId(creativeId).businessInfo(businessInfo)
                                .extraInfo(extraInfo).creator(Constants.APIDEFAULTUSER).modifier(Constants.APIDEFAULTUSER).build();
                        tbMobileDataMapper.insertSelective(tbMobileData);

                        Map<String, Object> resultMap = JsonUtil.toMap(businessInfo);

                        batchSaveToDetail(resultMap, userId, creativeId, tbMobileData.getId());
                    }else{

                        Integer mobileDataId = existingTbMobileData.getId();

                        tbMobileDataDetailMapper.deleteDataByMobileDataId(mobileDataId);

                        Map<String, Object> resultMap = JsonUtil.toMap(businessInfo);

                        batchSaveToDetail(resultMap, userId, creativeId, mobileDataId);

                        existingTbMobileData.setBusinessInfo(businessInfo);

                        existingTbMobileData.setExtraInfo(extraInfo);

                        tbMobileDataMapper.updateByPrimaryKeySelective(existingTbMobileData);
                    }

                    return true;

                }catch(Exception e){

                    log.error("ApiServiceImpl.mobileModify ==> save info to database error!!",e);

                    throw new ServiceException("保存数据库报错");

                }
            }
        });

    }

    public void batchSaveToDetail(Map<String,Object> mapData,String userId,Integer creativeId,Integer mobileDataId){

        List<TbMobileDataDetail> detailList = new ArrayList<>();

        for (Map.Entry<String, Object> entry : mapData.entrySet()) {

            TbMobileDataDetail detail = new TbMobileDataDetail();

            detail.setIsDeleted("N");

            detail.setCreativeId(creativeId);

            detail.setCreator("system");

            detail.setDataKey(entry.getKey());

            detail.setDataValue(entry.getValue().toString());

            detail.setMobileDataId(mobileDataId);

            detail.setModifier("system");

            detail.setUserId(userId);

            detailList.add(detail);
        }

        tbMobileDataDetailMapper.batchInsertDetail(detailList);

    }

    @Override
    public TbMobileData mobileQuery(String userId, Integer creativeId, String extraInfo) {
        return tbMobileDataMapper.selectByUserIdAndCreativeId(userId,creativeId);
    }

    @Override
    public JSONObject queryInfoByCondition(Integer creativeId, String businessInfo) throws Exception{

//        JSONObject json = JSONObject.parseObject(businessInfo);
//
//        String dataString = json.get("data").toString();

        JSONArray sqlListJson = JSONArray.parseArray(businessInfo);

        int sqlListsize = sqlListJson.size();

        JSONArray jsonArr = new JSONArray();

        for(int index = 0; index < sqlListsize;index++){

            String sqlString = Json2SqlConverter.convert4TbMobileDataDetail(sqlListJson.get(index).toString());

            if(null != creativeId){

                sqlString = sqlString + " and creative_id = '"+creativeId+"'";
            }
            sqlString = sqlString + " and is_deleted = 'N'";
            Integer count = tbCommonMapper.queryCountBySql(sqlString);

            jsonArr.add(count);
        }

        JSONObject jsonRes = new JSONObject();

        jsonRes.put("resCode",Constants.SUCESSCODE);

        jsonRes.put("resMsg",Constants.COMMONSUCCESSMSG);

        jsonRes.put("attachInfo","");

        jsonRes.put("commonResult",jsonArr);

        return jsonRes;

    }

    @Override
    public List<String> preloadLaunchInfoByVideoId(String videoId) {
        final String REDIS_KEY = PRELOAD_VIDEO_ID_REDIS_KEY_PREFIX + videoId;
        byte[] bytes = redisManager.get((REDIS_KEY).getBytes());
        if(bytes!=null){
            String cachedJsonString = new String(bytes);
            if(cachedJsonString != null){
                return JSON.parseObject(cachedJsonString,List.class);
            }
        }

        List<Byte> launchTimeTypeList = tbLaunchPlanMapper.selectLaunchTimeTypeByVideoId(videoId);
        if(CollectionUtils.isEmpty(launchTimeTypeList)){
            return new ArrayList<>();
        }

        Set<String> creativeFileUrlSet = new HashSet<>(128);

        for(Byte launchTimeType : launchTimeTypeList){
            if(LaunchTimeTypeEnum.VIDIO_TIME.getValue().equals(launchTimeType)){
                creativeFileUrlSet.addAll(tbLaunchPlanMapper.selectCreativeFileUrlWithVideoTimeByVideoId(videoId));
            }else if(LaunchTimeTypeEnum.REAL_TIME.getValue().equals(launchTimeType)){
                creativeFileUrlSet.addAll(tbLaunchPlanMapper.selectCreativeFileUrlWithRealTimeByVideoId(videoId));
            }else if(LaunchTimeTypeEnum.BJ_TIME.getValue().equals(launchTimeType)){
                Date now = new Date();
                List<Map<String,Object>> launchPlanMapList = tbLaunchPlanMapper.selectCreativeFileUrlWithBjTimeByVideoId(videoId);
                LocalDateTime dateTimeLowerLimit = LocalDateTime.now();
                LocalDateTime dateTimeUpperLimit = LocalDateTime.now().plusHours(4);
                for(Map<String,Object> launchTimeAndFileUrlMap : launchPlanMapList){
                    List<String> launchTimeList = JsonUtil.parseLaunchTimeString(launchTimeAndFileUrlMap.get("launch_time").toString());
                    for(String launchTime : launchTimeList){
                        String[] hourMinuteArr = launchTime.split(":");
                        Integer hour = Integer.parseInt(hourMinuteArr[0]);
                        Integer minute = Integer.parseInt(hourMinuteArr[1]);
                        LocalDateTime localDateTimeToday = LocalDateTime.of(LocalDate.now(),LocalTime.of(hour,minute));
                        LocalDateTime localDateTimeTomorrow = localDateTimeToday.plusDays(1);
                        //如果 今日投放时间 在预加载时间区间，则进行预加载。
                        if(dateTimeLowerLimit.isBefore(localDateTimeToday) && dateTimeUpperLimit.isAfter(localDateTimeToday)){
                            creativeFileUrlSet.add(launchTimeAndFileUrlMap.get("file_url").toString());
                        }
                        //考虑到跨天的情况，如果 投放截止日期不等于今天(说明 投放日期明天也是存在的) 并且 明日投放时间 在预加载时间区间，则进行预加载。
                        if(!DateUtil.toShortDateString((Date)launchTimeAndFileUrlMap.get("launch_date_end")).equals(DateUtil.toShortDateString(now))
                            &&
                            dateTimeLowerLimit.isBefore(localDateTimeTomorrow) && dateTimeUpperLimit.isAfter(localDateTimeTomorrow)
                        ){
                            creativeFileUrlSet.add(launchTimeAndFileUrlMap.get("file_url").toString());
                        }
                    }
                }
            }
        }
        List<String> result = new ArrayList<>(creativeFileUrlSet);
        redisManager.set(REDIS_KEY.getBytes(),JSON.toJSONString(result).getBytes());
        return result;
    }

}
