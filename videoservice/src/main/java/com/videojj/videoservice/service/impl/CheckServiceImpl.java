package com.videojj.videoservice.service.impl;

import com.google.gson.Gson;
import com.videojj.videocommon.exception.ServiceException;
import com.videojj.videocommon.util.DateUtil;
import com.videojj.videoservice.dao.*;
import com.videojj.videoservice.dto.AddLaunchPlanRequestDTO;
import com.videojj.videoservice.entity.*;
import com.videojj.videoservice.enums.CreativeStatusEnum;
import com.videojj.videoservice.enums.IsDeletedEnum;
import com.videojj.videoservice.service.CheckService;
import com.videojj.videoservice.service.LaunchPlanService;
import com.videojj.videoservice.util.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 判断是否在使用，true表示在使用，false表示不在使用
 *
 * @Author @videopls.com
 * Created by  on 2018/8/27 下午4:01.
 * @Description:
 */
@Service
public class CheckServiceImpl implements CheckService{

    @Resource
    private TbCreativeMapper tbCreativeMapper;

    @Resource
    private TbCreativeFileMapper tbCreativeFileMapper;

    @Resource
    private TbInteractionMapper tbInteractionMapper;

    @Resource
    private TbLaunchPlanMapper tbLaunchPlanMapper;

    @Resource
    private LaunchPlanService launchPlanService;

    private static Logger log = LoggerFactory.getLogger("CheckServiceImpl");

    private static Gson gson = new Gson();

    @Override
    public boolean isInUseTemplateId(Integer templateId) {

        TbCreativeCriteria param = new TbCreativeCriteria();

        TbCreativeCriteria.Criteria creativeCri = param.createCriteria();

        creativeCri.andTemplateIdEqualTo(templateId);

        creativeCri.andIsDeletedEqualTo(IsDeletedEnum.NO.getValue());

        creativeCri.andStatusEqualTo(CreativeStatusEnum.USE.getValue());

        List<TbCreative> creativeList =  tbCreativeMapper.selectByParam(param);

        if(CollectionUtils.isNotEmpty(creativeList)){

            return true;
        }else {

            return false;
        }
    }

    @Override
    public boolean isInUseInteractionId(Integer interactionId) {

        TbCreativeCriteria param = new TbCreativeCriteria();

        TbCreativeCriteria.Criteria creativeCri = param.createCriteria();

        creativeCri.andInteractionIdEqualTo(interactionId);

        creativeCri.andIsDeletedEqualTo(IsDeletedEnum.NO.getValue());

        creativeCri.andStatusEqualTo(CreativeStatusEnum.USE.getValue());

        List<TbCreative> creativeList =  tbCreativeMapper.selectByParam(param);

        if(CollectionUtils.isNotEmpty(creativeList)){

            return true;
        }else {

            return false;
        }
    }

    @Override
    public boolean isInUseCreativeId(Integer creativeId) {

        TbCreativeCriteria param = new TbCreativeCriteria();

        TbCreativeCriteria.Criteria creativeCri = param.createCriteria();

        creativeCri.andIdEqualTo(creativeId);

        creativeCri.andIsDeletedEqualTo(IsDeletedEnum.NO.getValue());

        creativeCri.andStatusEqualTo(CreativeStatusEnum.USE.getValue());

        List<TbCreative> creativeList =  tbCreativeMapper.selectByParam(param);

        if(CollectionUtils.isNotEmpty(creativeList)){

            return true;
        }else {

            return false;
        }
    }

    @Override
    public boolean isInUseCreativeUrl(String fileUrl) {

        TbCreativeFileCriteria fileCri = new TbCreativeFileCriteria();

        TbCreativeFileCriteria.Criteria tbcreaFile = fileCri.createCriteria();

        tbcreaFile.andFileUrlEqualTo(fileUrl);

        tbcreaFile.andIsDeletedEqualTo(IsDeletedEnum.NO.getValue());

        List<TbCreativeFile> tbFileList = tbCreativeFileMapper.selectByCriteria(fileCri);

        if(!CollectionUtils.isNotEmpty(tbFileList)){

            throw new ServiceException("该文件路径不存在");
        }else{

            return isInUseCreativeId(tbFileList.get(0).getCreativeId());
        }

    }

    @Override
    public boolean isInUseTemplateName(String interactionTypeName) {

        TbInteraction condition = new TbInteraction();
        condition.setInteractionTypeName(interactionTypeName);
        condition.setIsDeleted(IsDeletedEnum.NO.getValue());

        List<TbInteraction> interList = tbInteractionMapper.select(condition);
        if(CollectionUtils.isEmpty(interList)){
            return true;
        }else{
            return isInUseInteractionId(interList.get(0).getId());
        }

    }

    @Override
    public void updateUseStatus(Integer launchPlanId) {

        TbLaunchPlan tbLaunchPlan = tbLaunchPlanMapper.selectByPrimaryKey(launchPlanId);

        List<TbLaunchPlan> tbLaunchPlanList = tbLaunchPlanMapper.selectInUseByCreativeId(tbLaunchPlan.getCreativeId());

        if(CollectionUtils.isEmpty(tbLaunchPlanList)) {

            TbCreative creativeDo = new TbCreative();
            creativeDo.setId(tbLaunchPlan.getCreativeId());
            /**状态更新为未使用*/
            creativeDo.setStatus(CreativeStatusEnum.NO_USE.getValue());
            tbCreativeMapper.updateByPrimaryKeySelective(creativeDo);
        }
    }

    @Override
    public boolean isInUseInteractionTypeName(String interactionTypeName) {

        TbInteraction condition = new TbInteraction();
        condition.setInteractionTypeName(interactionTypeName);
        condition.setIsDeleted(IsDeletedEnum.NO.getValue());

        List<TbInteraction> inters = tbInteractionMapper.select(condition);

        if(CollectionUtils.isNotEmpty(inters)) {

            return isInUseInteractionId(inters.get(0).getId());
        }else{

            throw new ServiceException("该名称不存在");
        }

    }

    /**
        验证投放计划
     */
    @Override
    public String checkLaunchPlan(AddLaunchPlanRequestDTO request) {

        // 1、查询出来这个videoId的所有的投放，如果是空，就直接放回空
        List<String> planNameList = new ArrayList<>();

        for(String launchVideoId : launchPlanService.splitLaunchVideoIds(request.getLaunchVideoId())){
            List<TbLaunchPlan> planList = tbLaunchPlanMapper.selectOnlineByLaunchVideoId(launchVideoId);

            if(CollectionUtils.isEmpty(planList)){
                continue;
            }
            try {

                Date launchStartDate = DateUtil.parseShortDateString(request.getLaunchDateStart());
                Date launchEndDate = DateUtil.parseShortDateString(request.getLaunchDateEnd());
                // 2、判断新加的这个是哪种投放类型
                if (request.getLaunchTimeType().intValue() == 0) {
                    // 3、视频时间的，是一种特殊的时间，处理方式也可以特殊一点，就是判断日期是否有交叉，如果有交叉，然后把时间转成秒，根据秒进行判断
                    for(TbLaunchPlan plan:planList){
                        /**首先确定，日期是有交叉的，不然就不需要看了*/
                        if((DateUtil.compare(launchStartDate,plan.getLaunchDateStart())>=0&&DateUtil.compare(launchStartDate,plan.getLaunchDateEnd())<=0)
                                ||(DateUtil.compare(launchEndDate,plan.getLaunchDateStart())>=0&&DateUtil.compare(launchEndDate,plan.getLaunchDateEnd())<=0)
                                ||(DateUtil.compare(launchStartDate,plan.getLaunchDateStart())<0&&DateUtil.compare(launchEndDate,plan.getLaunchDateEnd())>0)){

                            /**如果确定日期有交叉，那就看时间是否有交叉，时间的交叉很容易，全部转换成秒就可以先转换请求的*/
                            List<Map<String,Integer>> reqTimeMapList = produceVideoLaunchTimeInfo(gson.toJson(request.getLaunchTime()),request.getLaunchLenTime());

                            List<Map<String,Integer>> existTimeMapList = produceVideoLaunchTimeInfo(plan.getLaunchTime(),plan.getLaunchLenTime());

                            Boolean checkResult = checkCoincide(reqTimeMapList,existTimeMapList);

                            if(checkResult){

                                planNameList.add(plan.getLaunchPlanName());
                            }
                        }
                    }
                }
                if(request.getLaunchTimeType().intValue() == 1) {
                    // 4、如果是即时投放的，那么有可能冲突的有两种情况，一种是即时的，一种是北京时间的

                    for (TbLaunchPlan plan : planList) {

                        Date realTime = new Date();
                        /**里面存储开始时间和结束时间，但是都是以秒来进行存储，判断的时候，就判断新增加的计划的秒数是否在已经存在的计划的区间内就可以了*/
                        Map<String, Long> reqTimeMapList = produceRealTimeDateToLaunchTimeInfo(realTime, request.getLaunchLenTime());

                        List<Map<String,Long>> existTimeMapList = null;

                        /**如果查询到的是1，那么就是有可能是直播即时投放的场景下*/
                        if (plan.getLaunchTimeType().intValue() == 1){

                            Map<String,Long> exists = produceRealTimeDateToLaunchTimeInfo(plan.getGmtCreated(),plan.getLaunchLenTime());

                            existTimeMapList = new ArrayList<>();

                            existTimeMapList.add(exists);
                        }
                        /**查询到的是2，那么就是有可能是北京时间投放的*/
                        else if(plan.getLaunchTimeType() ==2){

                            existTimeMapList = produceBJDateToLaunchTimeInfo(plan.getLaunchDateStart(),plan.getLaunchDateEnd(),plan.getLaunchTime(),plan.getLaunchLenTime());
                        }else{
                            continue;
                        }

                        Boolean checkResult = checkRealTimeCoincide(reqTimeMapList,existTimeMapList);

                        if(checkResult){

                            planNameList.add(plan.getLaunchPlanName());
                        }
                    }

                }

                if(request.getLaunchTimeType().intValue() == 2) {
                    for (TbLaunchPlan plan : planList) {
                        // 5、北京时间的 逻辑和即时投放的逻辑是一样的，但是有一点区别，就是北京时间的，可能投放时间会有多个，
                        List<Map<String, Long>> reqTimeMapList  = produceBJDateToLaunchTimeInfo(DateUtil.parseShortDateString(request.getLaunchDateStart())
                                ,DateUtil.parseShortDateString(request.getLaunchDateEnd()),gson.toJson(request.getLaunchTime()),request.getLaunchLenTime());

                        List<Map<String,Long>> existTimeMapList = null;
                        if (plan.getLaunchTimeType().intValue() == 1){

                            existTimeMapList = new ArrayList<>();

                            Map<String,Long> exists = produceRealTimeDateToLaunchTimeInfo(plan.getGmtCreated(),plan.getLaunchLenTime());

                            existTimeMapList.add(exists);
                        }
                        /**如果查询到的是2，那么就是有可能是北京时间投放的*/
                        else if(plan.getLaunchTimeType() ==2){

                            existTimeMapList = produceBJDateToLaunchTimeInfo(plan.getLaunchDateStart(),plan.getLaunchDateEnd(),plan.getLaunchTime(),plan.getLaunchLenTime());
                        }else{
                            /**理论上来讲，不可能北京时间或者即时投放的计划，与视频时间投放的计划的id一摸一样，但是为了防止特殊情况发生。。*/
                            continue;
                        }

                        Boolean checkResult = checkBJTimeCoincide(reqTimeMapList,existTimeMapList);

                        if(checkResult){

                            planNameList.add(plan.getLaunchPlanName());
                        }

                    }

                }

            }catch (Exception e){

                log.error("CheckServiceImpl.checkLaunchPlan ==> check数据报错了",e);

                throw new ServiceException("check 数据报错了");

            }


        }

        return String.join(",", planNameList);
    }

    private Boolean checkBJTimeCoincide(List<Map<String, Long>> reqTimeMapList, List<Map<String, Long>> existTimeMapList) {

        for(Map<String,Long> req:reqTimeMapList){

            for(Map<String,Long>exist:existTimeMapList){

                if((req.get("startSecond")>=exist.get("startSecond")&&req.get("startSecond")<=exist.get("endSecond"))
                        ||(req.get("endSecond")>=exist.get("startSecond")&&req.get("endSecond")<=exist.get("endSecond"))
                        ||(req.get("startSecond")<exist.get("startSecond")&&req.get("endSecond")>exist.get("endSecond"))){

                    return true;

                }
            }
        }

        return false;
    }

    private Boolean checkRealTimeCoincide(Map<String, Long> reqTimeMap, List<Map<String, Long>> existTimeMapList) {

        for(Map<String, Long> timeMap:existTimeMapList){

            if((reqTimeMap.get("startSecond")>=timeMap.get("startSecond")&&reqTimeMap.get("startSecond")<=timeMap.get("endSecond"))
                    ||(reqTimeMap.get("endSecond")>=timeMap.get("startSecond")&&reqTimeMap.get("endSecond")<=timeMap.get("endSecond"))
                    ||(reqTimeMap.get("startSecond")<timeMap.get("startSecond")&&reqTimeMap.get("endSecond")>timeMap.get("endSecond"))){

                return true;

            }
        }
        return false;
    }

    private List<Map<String, Long>> produceBJDateToLaunchTimeInfo(Date launchDateStart,Date launchDateEnd,String launchTime, String launchLenTime) {

        List<Map<String,Long>> timeMapList = new ArrayList<>();

        while(DateUtil.compare(launchDateStart,launchDateEnd)<=0){
            // 时间格式拆分更改
            List<String> launchTimeList = JsonUtil.parseLaunchTimeString(launchTime);

            for(String singleLaunchTime:launchTimeList){

                String[] launchTimeArray = singleLaunchTime.split(":");

                Date launchBJTime = DateUtil.addMinutes(DateUtil.addHours(launchDateStart,Integer.parseInt(launchTimeArray[0])),Integer.parseInt(launchTimeArray[1]));

                Long start = launchBJTime.getTime()/1000l;

                Long end = start+Long.parseLong(launchLenTime);

                Map<String,Long> res = new HashMap<>();

                res.put("startSecond",start);

                res.put("endSecond",end);

                timeMapList.add(res);

            }
            launchDateStart = DateUtil.addDays(launchDateStart,1);
        }
        return timeMapList;

    }

    private Map<String,Long> produceRealTimeDateToLaunchTimeInfo(Date date, String launchLenTime) {

        /**自己计算时间，就会有一个问题，跨日的情况*/
        Long start = date.getTime()/1000l;

        Long end = start + Long.parseLong(launchLenTime);

        Map<String,Long> res = new HashMap<>();

        res.put("startSecond",start);

        res.put("endSecond",end);

        return res;

    }

    private Boolean checkCoincide(List<Map<String, Integer>> reqTimeMapList, List<Map<String, Integer>> existTimeMapList) {

        for(Map<String,Integer>req:reqTimeMapList){
            for(Map<String,Integer>exist:existTimeMapList){

                if((req.get("startSecond")>=exist.get("startSecond")&&req.get("startSecond")<=exist.get("endSecond"))
                        ||(req.get("endSecond")>=exist.get("startSecond")&&req.get("endSecond")<=exist.get("endSecond"))
                        ||(req.get("startSecond")<exist.get("startSecond")&&req.get("endSecond")>exist.get("endSecond"))){

                    return true;

                }
            }

        }

        return false;

    }

    private List<Map<String,Integer>> produceVideoLaunchTimeInfo(String launchTimeStr, String launchLenTime) {
        // 时间格式拆分更改
//        List<String> launchTimeList = Arrays.asList(launchTimeStr.split(","));

        List<String> launchTimeList = JsonUtil.parseLaunchTimeString(launchTimeStr);

        List<Map<String,Integer>> timeMapList = new ArrayList<>();

        for(String singleLaunchTime:launchTimeList){

            String[] launchTimeArray = singleLaunchTime.split(":");

            Integer startSecond = Integer.parseInt(launchTimeArray[0])*60+Integer.parseInt(launchTimeArray[1]);

            Integer endSecond = startSecond+Integer.parseInt(launchLenTime);

            Map<String,Integer> timeMap = new HashMap<>();

            timeMap.put("startSecond",startSecond);

            timeMap.put("endSecond",endSecond);

            timeMapList.add(timeMap);

        }
        return timeMapList;
    }
}
