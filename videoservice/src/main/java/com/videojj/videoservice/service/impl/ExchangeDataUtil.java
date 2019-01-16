package com.videojj.videoservice.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.videojj.videocommon.exception.ServiceException;
import com.videojj.videocommon.util.DateUtil;
import com.videojj.videoservice.apidto.LaunchApiQueryInfoResponseDTO;
import com.videojj.videoservice.dto.MonitorLinkDTO;
import com.videojj.videoservice.entity.TbLaunchPlanApiInfoExt;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @Author @videopls.com  投放时间是时分
 * Created by  on 2018/8/30 下午3:26.
 * @Description:
 */
public class ExchangeDataUtil {

    private static org.slf4j.Logger log = LoggerFactory.getLogger("ExchangeDataUtil");

    private static Gson gson = new Gson();

    public static List<LaunchApiQueryInfoResponseDTO.LaunchInfo> convertResult(List<TbLaunchPlanApiInfoExt> launchPlanList) throws Exception{

        List<LaunchApiQueryInfoResponseDTO.LaunchInfo> launchInfoList = new ArrayList<>();

        for(TbLaunchPlanApiInfoExt tbLaunchPlanApiInfoExt:launchPlanList) {

            String launchTimeStr = tbLaunchPlanApiInfoExt.getLaunchTime();

            // 此处的解析进行修改一下  还需要增加返回总热点数，当前热点数 以及素材id
//            List<String> launchTimeList = Arrays.asList(launchTimeStr.split(","));

            JSONArray jsonArray = JSONArray.parseArray(launchTimeStr);

            int sumHotSpot = tbLaunchPlanApiInfoExt.getHotSpotNum();
            /**如果是即时投放的话，那么投放时间就是空，投放时长是一直都会有的，所以这里其实是有点问题的*/
            for(int outIndex=0;outIndex<jsonArray.size();outIndex++){

                int hotSpotOrder = outIndex;

                JSONArray innerArray = JSONArray.parseArray(jsonArray.get(outIndex).toString());

                // 此处分为两层循环，外层循环是记录下第几个热点，只要计数就好，第一次循环就是1，第二次就是2，内层循环是具体的时间的循环，
                for(int index = 0;index<innerArray.size();index++){

                    String launchTime = innerArray.get(index).toString();

                    List<String> hourAndMin = Arrays.asList(launchTime.split(":"));

                    Integer hour = null;

                    Integer minite = null;

                    if(hourAndMin.get(0).length() == 2 && hourAndMin.get(0).startsWith("0")){

                        hour = Integer.valueOf(String.valueOf(hourAndMin.get(0).charAt(1)));
                    }else{

                        hour = Integer.valueOf(hourAndMin.get(0));
                    }

                    if(hourAndMin.get(1).length() == 2 && hourAndMin.get(1).startsWith("0")){

                        minite = Integer.valueOf(String.valueOf(hourAndMin.get(1).charAt(1)));
                    }else{

                        minite = Integer.valueOf(hourAndMin.get(1));
                    }

                    LaunchApiQueryInfoResponseDTO.LaunchInfo launchInfo = new LaunchApiQueryInfoResponseDTO.LaunchInfo();

                    launchInfo.setHotspotTrackLink(gson.fromJson(tbLaunchPlanApiInfoExt.getHotspotTrackLink(),new TypeToken<List<MonitorLinkDTO>>(){}.getType()));

                    launchInfo.setInfoTrackLink(gson.fromJson(tbLaunchPlanApiInfoExt.getInfoTrackLink(),new TypeToken<List<MonitorLinkDTO>>(){}.getType()));

                    launchInfo.setSumHotspot(sumHotSpot);

                    launchInfo.setHotspotOrder(hotSpotOrder);

                    launchInfo.setCreativeId(tbLaunchPlanApiInfoExt.getSourceId());

                    launchInfo.setTemplate(tbLaunchPlanApiInfoExt.getTemplateFileName());

                    launchInfo.setData(JSONObject.parseObject(tbLaunchPlanApiInfoExt.getMaterialContent()));
                    /**对于即时投放的，只有一个时长*/
                    if (StringUtils.isNotEmpty(tbLaunchPlanApiInfoExt.getLaunchLenTime())) {

                        launchInfo.setDuration(Long.valueOf(tbLaunchPlanApiInfoExt.getLaunchLenTime()) * 1000L);
                    }

                    /**点播的情况，其实传进来的是分秒*/
                    if(tbLaunchPlanApiInfoExt.getLaunchTimeType().intValue() == 0) {

                        Long innerlminite = Long.parseLong(minite.toString())  * 1000L;

                        Long innerlh = Long.parseLong(hour.toString())  *60L * 1000L;

                        Long innerstartTime = innerlminite + innerlh;

                        Long innerendTime = 0L;

                        if (null != launchInfo.getDuration()) {

                            innerendTime = innerstartTime + launchInfo.getDuration();
                        } else {

                            innerendTime = innerstartTime;
                        }

                        launchInfo.setVideoStartTime(innerstartTime);

                        launchInfo.setVideoEndTime(innerendTime);
                    }
                    /**直播时北京时间投放*/
                    try {
                        if (tbLaunchPlanApiInfoExt.getLaunchTimeType().intValue() == 2) {

                            Date start = DateUtil.addMinutes(DateUtil.addHours(DateUtil.getStartDate(new Date()), hour), minite);

                            launchInfo.setClockStartTime(DateUtil.toLongDateString(start));

                            if (null != launchInfo.getDuration()) {

                                launchInfo.setClockEndTime(DateUtil.toLongDateString(DateUtil.addSeconds(start, Integer.valueOf(launchInfo.getDuration().toString()))));

                            }else{

                                launchInfo.setClockEndTime(launchInfo.getClockStartTime());
                            }
                        }
                    }catch (Exception e){

                        log.error("ExchangeDataUtil.convertResult ==>  exchange launchDate error!! videoId is {}",tbLaunchPlanApiInfoExt.getLaunchVideoId());

                        throw new ServiceException("数据转换时报错。。。。");
                    }

                    launchInfo.setId(UUID.randomUUID().toString());

                    launchInfoList.add(launchInfo);

                }


            }



        }
        return launchInfoList;

    }

    public static List<LaunchApiQueryInfoResponseDTO.LaunchInfo> convertRealTimeResult(List<TbLaunchPlanApiInfoExt> launchPlanList,String jobName,Boolean isRealTime) throws Exception{

        List<LaunchApiQueryInfoResponseDTO.LaunchInfo> launchInfoList = new ArrayList<>();

        TbLaunchPlanApiInfoExt launchPlanApiInfoExt = launchPlanList.get(0);

        LaunchApiQueryInfoResponseDTO.LaunchInfo launchInfo = new LaunchApiQueryInfoResponseDTO.LaunchInfo();

        launchInfo.setId(UUID.randomUUID().toString());

        launchInfo.setData(JSONObject.parseObject(launchPlanApiInfoExt.getMaterialContent()));

        launchInfo.setDuration(Long.valueOf(launchPlanApiInfoExt.getLaunchLenTime()) * 1000L);

        launchInfo.setTemplate(launchPlanApiInfoExt.getTemplateFileName());

        /**如果是北京时间的，才知道是第几个热点的，否则的话，即时北京时间，需要知道热点数及顺序*/
        if(!isRealTime){

            String launchTimeStr = launchPlanApiInfoExt.getLaunchTime();

            JSONArray jsonArray = JSONArray.parseArray(launchTimeStr);

            int sumHotSpot = launchPlanApiInfoExt.getHotSpotNum();

//            int sumHotSpot = jsonArray.size();

            launchInfo.setSumHotspot(sumHotSpot);

//            String time = jobName.substring(jobName.length() - 5,jobName.length());

            String time = jobName.split("-")[1];

            for(int i=0;i<sumHotSpot;i++){

                if(jsonArray.get(i).toString().contains(time)){

//                    int order = i+1;
                    /**客户端会自动给加一的*/
                    launchInfo.setHotspotOrder(i);
                }
            }
        }else{

            launchInfo.setHotspotOrder(1);

            launchInfo.setSumHotspot(1);

        }
        launchInfo.setCreativeId(launchPlanApiInfoExt.getSourceId());

        launchInfo.setHotspotTrackLink(gson.fromJson(launchPlanApiInfoExt.getHotspotTrackLink(),new TypeToken<List<MonitorLinkDTO>>(){}.getType()));

        launchInfo.setInfoTrackLink(gson.fromJson(launchPlanApiInfoExt.getInfoTrackLink(),new TypeToken<List<MonitorLinkDTO>>(){}.getType()));

        launchInfoList.add(launchInfo);

        return launchInfoList;
    }
}
