package com.videojj.videoservice.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.Page;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.videojj.videocommon.constant.Constants;
import com.videojj.videocommon.exception.ServiceException;
import com.videojj.videocommon.util.BeanUtils;
import com.videojj.videocommon.util.DateUtil;
import com.videojj.videoservice.annotation.OperationLogAnnotationService;
import com.videojj.videoservice.cache.LaunchPlanCache;
import com.videojj.videoservice.dao.*;
import com.videojj.videoservice.dto.*;
import com.videojj.videoservice.emqttd.MqttService;
import com.videojj.videoservice.entity.*;
import com.videojj.videoservice.enums.*;
import com.videojj.videoservice.handler.SendEmqService;
import com.videojj.videoservice.service.CheckService;
import com.videojj.videoservice.service.LaunchPlanService;
import com.videojj.videoservice.service.OperationLogService;
import com.videojj.videoservice.thread.RealTimeAutoCloseThread;
import com.videojj.videoservice.thread.SendEmqThread;
import com.videojj.videoservice.util.ConcurrencyUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import com.videojj.videoservice.util.PermissionUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.crazycake.shiro.RedisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.videojj.videocommon.constant.Constants.PRELOAD_VIDEO_ID_REDIS_KEY_PREFIX;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/7 下午7:02.
 * @Description:
 */
@Service
public class LaunchPlanServiceImpl implements LaunchPlanService{

    @Resource
    private TbLaunchPlanMapper tbLaunchPlanMapper;

    @Resource
    private TbCreativeMapper tbCreativeMapper;

    private static Logger log = LoggerFactory.getLogger("LaunchPlanServiceImpl");

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private CheckService checkService;

    @Resource
    private TbCreativeImageMapper tbCreativeImageMapper;

    @Resource
    private MqttService mqttService;

    @Autowired
    protected SendEmqService sendEmqService;

    @Autowired
    private QuartzServiceHelper quartzServiceHelper;

    @Resource
    private OperationLogService operationLogService;

    @Resource
    private TbLaunchPlanOperationMapper tbLaunchPlanOperationMapper;

    @Resource
    private LaunchPlanCache launchPlanCache;

    @Resource
    private RedisManager redisManager;

    @Resource
    private RabbitTemplate myRabbitmqTemplate;

    private static Gson gson = new Gson();

    public static void main(String[] args) {
        gson.toJson(null);
    }
    private static ThreadPoolExecutor executors = ConcurrencyUtil.newThreadPoolExecutor(20, 50, 0L, TimeUnit.SECONDS,
            new LinkedBlockingDeque<Runnable>(5000), "LaunchPlanServiceImpl-");

    @Override
    public boolean existsSameLaunchPlanOperationName(String launchPlanOperationName) {
        TbLaunchPlanOperation tbLaunchPlanOperation = new TbLaunchPlanOperation();
        tbLaunchPlanOperation.setLaunchPlanName(launchPlanOperationName);
        return CollectionUtils.isNotEmpty(tbLaunchPlanOperationMapper.select(tbLaunchPlanOperation));
    }

    @OperationLogAnnotationService(descArgPositions = {0},fieldNames = {"launchPlanName"},type = OperationLogTypeEnum._41)
    @Override
    public void addLaunchPlanService(AddLaunchPlanRequestDTO request){

        if(existsSameLaunchPlanOperationName(request.getLaunchPlanName())){
            throw new ServiceException("名称重复，重新命名投放计划名称");
        }

        TbLaunchPlanOperation tbLaunchPlanOperation = new TbLaunchPlanOperation();
        try {

            if (StringUtils.isNotEmpty(request.getLaunchDateStart())) {
                tbLaunchPlanOperation.setLaunchDateStart(DateUtil.parseShortDateString(request.getLaunchDateStart()));
            }else{

                tbLaunchPlanOperation.setLaunchDateStart(new Date());
            }
            if (StringUtils.isNotEmpty(request.getLaunchDateEnd())) {
                tbLaunchPlanOperation.setLaunchDateEnd(DateUtil.parseShortDateString(request.getLaunchDateEnd()));
            }else{

                tbLaunchPlanOperation.setLaunchDateEnd(DateUtils.addSeconds(tbLaunchPlanOperation.getLaunchDateStart(),Integer.parseInt(request.getLaunchLenTime())));
            }

        } catch (ParseException e) {
            log.error("LaunchPlanServiceImpl.addLaunchPlanService ==> launchDate is error!!", e);
            throw new ServiceException("投放日期格式有问题");
        }

        tbLaunchPlanOperation.setCreator(request.getUsername());
        tbLaunchPlanOperation.setInteractionId(request.getInteractionTypeId());
        tbLaunchPlanOperation.setIsDeleted(IsDeletedEnum.NO.getValue());

        tbLaunchPlanOperation.setLaunchLenTime(request.getLaunchLenTime());
        tbLaunchPlanOperation.setLaunchPlanName(request.getLaunchPlanName());
        tbLaunchPlanOperation.setLaunchTime(gson.toJson(request.getLaunchTime()));
        tbLaunchPlanOperation.setLaunchTimeType(request.getLaunchTimeType());

        tbLaunchPlanOperation.setCreativeId(request.getCreativeId());
        tbLaunchPlanOperation.setModifier(request.getUsername());
        if(CollectionUtils.isNotEmpty(request.getHotspotTrackLink())){
            tbLaunchPlanOperation.setHotspotTrackLink(gson.toJson(request.getHotspotTrackLink()));
        }
        if(CollectionUtils.isNotEmpty(request.getInfoTrackLink())){
            tbLaunchPlanOperation.setInfoTrackLink(gson.toJson(request.getInfoTrackLink()));
        }
        if(tbLaunchPlanOperation.getLaunchTimeType().intValue() != LaunchTimeTypeEnum.REAL_TIME.getValue().intValue()){
            tbLaunchPlanOperation.setStatus(LaunchStatusEnum.PASS.getValue());
        }else{
            tbLaunchPlanOperation.setStatus(LaunchStatusEnum.WAIT_PASS.getValue());
        }
        transactionTemplate.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean doInTransaction(TransactionStatus transactionStatus) {
                try {

                    TbCreativeImage creativeImage = new TbCreativeImage();
                    saveImage(request.getCreativeId(), creativeImage);
                    tbLaunchPlanOperation.setCreativeImageId(creativeImage.getId());
                    tbLaunchPlanOperationMapper.insertSelective(tbLaunchPlanOperation);

                    //插入投放计划表
                    for(String launchVideoId : splitLaunchVideoIds(request.getLaunchVideoId())){
                        TbLaunchPlan tbLaunchPlan = new TbLaunchPlan();
                        BeanUtils.copyProperties(tbLaunchPlanOperation,tbLaunchPlan,"id");
                        tbLaunchPlan.setLaunchVideoId(launchVideoId);
                        tbLaunchPlan.setOperationId(tbLaunchPlanOperation.getId());
                        tbLaunchPlanMapper.insertSelective(tbLaunchPlan);
                    }

                    if(tbLaunchPlanOperation.getLaunchTimeType().intValue() != LaunchTimeTypeEnum.REAL_TIME.getValue().intValue()){
                        updateForCreativeToUse(tbLaunchPlanOperation.getId());
                    }

                    return true;
                }catch(Exception e){
                    log.error("LaunchPlanServiceImpl.addLaunchPlanService ==> save info to database error!!",e);
                    throw new ServiceException("保存数据库报错");
                }
            }
        });

        if(tbLaunchPlanOperation.getLaunchTimeType().intValue() != LaunchTimeTypeEnum.REAL_TIME.getValue().intValue()){
            //投放
            launch(tbLaunchPlanOperation.getId());

        }
    }

    private void updateStatusForLaunchPlan(Byte status,Integer operationId){
        TbLaunchPlanOperation launchPlanOperation = new TbLaunchPlanOperation();
        launchPlanOperation.setStatus(status);
        launchPlanOperation.setId(operationId);
        tbLaunchPlanOperationMapper.updateByPrimaryKeySelective(launchPlanOperation);
        tbLaunchPlanMapper.updateStatusByOperationId(status,operationId);
    }

//投放计划投放操作
    @Override
    public void launchLaunchPlanService(LaunchLaunchPlanRequestDTO request){
        TbLaunchPlanOperation tbLaunchPlanOperation = tbLaunchPlanOperationMapper.selectByPrimaryKey(request.getLaunchPlanId());
        if (tbLaunchPlanOperation.getLaunchTimeType().intValue() == LaunchTimeTypeEnum.REAL_TIME.getValue().intValue()
            && tbLaunchPlanOperation.getStatus().intValue() == LaunchStatusEnum.WAIT_PASS.getValue().intValue()
        ) {

            transactionTemplate.execute(new TransactionCallback<Boolean>() {
                @Override
                public Boolean doInTransaction(TransactionStatus transactionStatus) {
                    try {
                        //更新投放计划的状态为上线中
                        updateStatusForLaunchPlan(LaunchStatusEnum.PASS.getValue(),tbLaunchPlanOperation.getId());
                        updateForCreativeToUse(tbLaunchPlanOperation.getId());
                        return true;

                    }catch(Exception e){
                        log.error("LaunchPlanServiceImpl.launchLaunchPlanService ==> save info to database error!!",e);
                        throw new ServiceException("保存数据库报错");
                    }
                }
            });

            //投放
            launch(tbLaunchPlanOperation.getId());

            /**暂时不需要进行下线操作，即时投放的投放计划，目前可以进行重复的投放*/
            //即时投放自动下线
            new RealTimeAutoCloseThread(this,
                    Long.parseLong(tbLaunchPlanOperation.getLaunchLenTime()),
                    new DeleteLaunchPlanRequestDTO(tbLaunchPlanOperation.getId(), PermissionUtil.getCurrentUsername()),tbLaunchPlanMapper,tbLaunchPlanOperationMapper
            ).start();

            // 记录操作日志
            operationLogService.writeOperationLog_43(tbLaunchPlanOperation.getLaunchPlanName());
        }
    }

    private void updateForCreativeToUse(Integer launchPlanOperationId){
        TbLaunchPlanOperation tbLaunchPlanOperation = tbLaunchPlanOperationMapper.selectByPrimaryKey(launchPlanOperationId);

        /**一期没有审核功能，增加之后，默认都是审核通过的，然后自动更新素材为使用中*/
        TbCreative record = new TbCreative();
        record.setId(tbLaunchPlanOperation.getCreativeId());
        record.setStatus(CreativeStatusEnum.USE.getValue());
        tbCreativeMapper.updateByPrimaryKeySelective(record);
    }

    private void launch(Integer launchPlanOperationId){
        TbLaunchPlanOperation tbLaunchPlanOperation = tbLaunchPlanOperationMapper.selectByPrimaryKey(launchPlanOperationId);
        List<TbLaunchPlan> tbLaunchPlanList = tbLaunchPlanMapper.selectByOperationId(launchPlanOperationId);

        //投放计划的投放处理
        /**如果是视频时间的，就直接返回就行了*/
        if (tbLaunchPlanOperation.getLaunchTimeType().intValue() == LaunchTimeTypeEnum.VIDIO_TIME.getValue().intValue()) {
            for(TbLaunchPlan tbLaunchPlan : tbLaunchPlanList) {

                launchPlanCache.updateRedis(tbLaunchPlan.getLaunchVideoId());
                //TODO
//                mqttService.pushVideoId("videoIdTopic", tbLaunchPlan.getLaunchVideoId());
                myRabbitmqTemplate.convertAndSend("fanoutExchange","",tbLaunchPlan.getLaunchVideoId());
            }
            return;
        }

        for(TbLaunchPlan tbLaunchPlan : tbLaunchPlanList){
            SendEmqThread thread = new SendEmqThread(tbLaunchPlan, quartzServiceHelper, tbLaunchPlanMapper, mqttService);
            executors.execute(thread);
        }
    }

    /**暂时还没有修改功能，等需要修改功能的时候，此处修改，需要素材镜像也一期修改*/
    @Override
    public void updateLaunchPlanService(UpdateLaunchPlanRequestDTO request) throws ParseException {
    }

    @Override
    public LaunchPlanPageInfoResponseDTO queryPageInfo(Integer interactionTypeId) {

        Page<TbLaunchPlanExt> page =  tbLaunchPlanMapper.selectPage(interactionTypeId);
        LaunchPlanPageInfoResponseDTO launchPlanPageInfoResponseDTO = new LaunchPlanPageInfoResponseDTO();
        launchPlanPageInfoResponseDTO.setResCode(Constants.SUCESSCODE);
        launchPlanPageInfoResponseDTO.setResMsg(Constants.COMMONSUCCESSMSG);

        if (page.getTotal() == 0){
            launchPlanPageInfoResponseDTO.setTotalPage(0);
            launchPlanPageInfoResponseDTO.setTotalRecord(0L);
            return launchPlanPageInfoResponseDTO;
        }

        launchPlanPageInfoResponseDTO.setTotalPage(page.getPages());
        launchPlanPageInfoResponseDTO.setTotalRecord(page.getTotal());
        launchPlanPageInfoResponseDTO.setLaunchPlanList(batchTransform(page));

        return launchPlanPageInfoResponseDTO;
    }

    @Override
    public LaunchPlanPageInfoResponseDTO queryOperationPageInfo(Integer interactionTypeId,Byte launchTimeType) {

        Page<TbLaunchPlanOperationExt> page =  tbLaunchPlanMapper.selectOperationPage(interactionTypeId,launchTimeType);
        LaunchPlanPageInfoResponseDTO launchPlanPageInfoResponseDTO = new LaunchPlanPageInfoResponseDTO();
        launchPlanPageInfoResponseDTO.setResCode(Constants.SUCESSCODE);
        launchPlanPageInfoResponseDTO.setResMsg(Constants.COMMONSUCCESSMSG);

        if (page.getTotal() == 0){
            launchPlanPageInfoResponseDTO.setTotalPage(0);
            launchPlanPageInfoResponseDTO.setTotalRecord(0L);
            return launchPlanPageInfoResponseDTO;
        }

        launchPlanPageInfoResponseDTO.setTotalPage(page.getPages());
        launchPlanPageInfoResponseDTO.setTotalRecord(page.getTotal());
        launchPlanPageInfoResponseDTO.setLaunchPlanList(batchTransformOperation(page));

        return launchPlanPageInfoResponseDTO;
    }

    @Override
    public void auditLaunchPlanService(AuditLaunchPlanRequestDTO request){

        TbLaunchPlan param = new TbLaunchPlan();

        param.setId(request.getLaunchPlanId());

        param.setModifier(request.getUsername());

        param.setStatus(request.getAuditStatus());

        param.setExtraInfo(request.getRemark());

        transactionTemplate.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean doInTransaction(TransactionStatus status) {
                try {

                    tbLaunchPlanMapper.updateByPrimaryKeySelective(param);

                    if(request.getAuditStatus().intValue() == LaunchStatusEnum.NOT_PASS.getValue().intValue()
                            ||request.getAuditStatus() == LaunchStatusEnum.CLOSE.getValue().intValue()){

                        checkService.updateUseStatus(request.getLaunchPlanId());

                    }
                }catch (Exception e){

                    log.error("LaunchPlanServiceImpl.auditLaunchPlanService ==> update error!!plan planid is {}",request.getLaunchPlanId(),e);

                    throw new ServiceException("更新数据库时报错");
                }
                return Boolean.TRUE;
            }
        });
    }

    @Override
    public void deleteLaunchPlanService(DeleteLaunchPlanRequestDTO request){

        TbLaunchPlan tbLaunchPlan = tbLaunchPlanMapper.selectByPrimaryKey(request.getLaunchPlanId());

        /**如果是北京时间的，那么就要判断*/
        if(tbLaunchPlan.getLaunchTimeType().intValue() == LaunchTimeTypeEnum.BJ_TIME.getValue().intValue()) {

            quartzServiceHelper.removeQuartz(tbLaunchPlan.getLaunchDateStart(),tbLaunchPlan.getLaunchDateEnd(),
                    tbLaunchPlan.getLaunchTime(),tbLaunchPlan);

        }

        TbLaunchPlan param = new TbLaunchPlan();

        param.setId(request.getLaunchPlanId());

        param.setModifier(request.getUsername());

        param.setIsDeleted(IsDeletedEnum.YES.getValue());

        transactionTemplate.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean doInTransaction(TransactionStatus status) {
                try {

                    tbLaunchPlanMapper.updateByPrimaryKeySelective(param);

                    checkService.updateUseStatus(request.getLaunchPlanId());

                }catch (Exception e){

                    log.error("LaunchPlanServiceImpl.deleteLaunchPlanService ==> update error!!plan planId is {}",request.getLaunchPlanId(),e);

                    throw new ServiceException("逻辑删除数据时报错");
                }
                return Boolean.TRUE;
            }
        });
    }

    @Override
    public LaunchPlanDetailInfoResponseDTO queryDetailInfoById(Integer launchPlan) {

        LaunchPlanDetailInfoResponseDTO res = new LaunchPlanDetailInfoResponseDTO();
        TbLaunchPlanOperationExt tbLaunchPlanExt = tbLaunchPlanOperationMapper.selectDetailInfoById(launchPlan);
        TbCreativeImageCriteria param = new TbCreativeImageCriteria();
        TbCreativeImageCriteria.Criteria creativeCri = param.createCriteria();
        creativeCri.andIsDeletedEqualTo("N").andSourceIdEqualTo(tbLaunchPlanExt.getCreativeId());
        List<TbCreativeImage> creativeImage = tbCreativeImageMapper.selectByCriteria(param);
        if(CollectionUtils.isNotEmpty(creativeImage)&&null != creativeImage.get(0).getHotSpotNum()){
            res.setHotSpotNum(creativeImage.get(0).getHotSpotNum());
        }else{
            res.setHotSpotNum(1);
        }

        res.setStatus(tbLaunchPlanExt.getStatus());
        res.setCreativeId(tbLaunchPlanExt.getCreativeId());
        res.setCreativeName(tbLaunchPlanExt.getCreativeName());
        res.setLaunchVideoId(tbLaunchPlanExt.getLaunchVideoId());
        res.setHotspotTrackLink(gson.fromJson(tbLaunchPlanExt.getHotspotTrackLink(),new TypeToken<List<MonitorLinkDTO>>(){}.getType()));
        res.setInfoTrackLink(gson.fromJson(tbLaunchPlanExt.getInfoTrackLink(),new TypeToken<List<MonitorLinkDTO>>(){}.getType()));

        JSONArray array = JSONArray.parseArray(tbLaunchPlanExt.getLaunchTime());
        List<List<String>> launchTime = new ArrayList<>();
        for(int i = 0;i<array.size();i++){
            List<String> innerList = new ArrayList<>();
            JSONArray innerArray = JSONArray.parseArray(array.get(i).toString());
            for(int j=0;j<innerArray.size();j++){
                innerList.add(innerArray.get(j).toString());
            }
            launchTime.add(innerList);
        }

        res.setLaunchTime(launchTime);
        res.setLaunchLenTime(tbLaunchPlanExt.getLaunchLenTime());
        res.setInteractionTypeId(tbLaunchPlanExt.getInteractionId());
        res.setInteractionTypeName(tbLaunchPlanExt.getInteractionName());
        res.setLaunchDateEnd(DateUtil.toShortDateString(tbLaunchPlanExt.getLaunchDateEnd()));
        res.setLaunchDateStart(DateUtil.toShortDateString(tbLaunchPlanExt.getLaunchDateStart()));
        res.setLaunchPlanName(tbLaunchPlanExt.getLaunchPlanName());
        res.setLaunchTimeType(tbLaunchPlanExt.getLaunchTimeType());
        res.setRemark(tbLaunchPlanExt.getExtraInfo());
        res.setResCode(Constants.SUCESSCODE);
        res.setResMsg(Constants.COMMONSUCCESSMSG);

        return res;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void offlineLaunchPlanService(DeleteLaunchPlanRequestDTO request) {
        TbLaunchPlanOperation tbLaunchPlanOperation = new TbLaunchPlanOperation();
        tbLaunchPlanOperation.setId(request.getLaunchPlanId());
        tbLaunchPlanOperation.setModifier(request.getUsername());
        tbLaunchPlanOperation.setStatus(LaunchStatusEnum.CLOSE.getValue());

        tbLaunchPlanOperationMapper.updateByPrimaryKeySelective(tbLaunchPlanOperation);
        List<TbLaunchPlan> tbLaunchPlanList = tbLaunchPlanMapper.selectByOperationId(request.getLaunchPlanId());
        for(TbLaunchPlan tbLaunchPlan : tbLaunchPlanList){

            /**如果是北京时间的，那么就要判断*/
            if(tbLaunchPlan.getLaunchTimeType().intValue() == LaunchTimeTypeEnum.BJ_TIME.getValue().intValue()) {

                quartzServiceHelper.removeQuartz(tbLaunchPlan.getLaunchDateStart(),tbLaunchPlan.getLaunchDateEnd(),
                        tbLaunchPlan.getLaunchTime(),tbLaunchPlan);
            }

            TbLaunchPlan param = new TbLaunchPlan();
            param.setId(tbLaunchPlan.getId());
            param.setModifier(request.getUsername());
            param.setStatus(LaunchStatusEnum.CLOSE.getValue());

            tbLaunchPlanMapper.updateByPrimaryKeySelective(param);
            checkService.updateUseStatus(tbLaunchPlan.getId());

            if (tbLaunchPlan.getLaunchTimeType().intValue() == LaunchTimeTypeEnum.VIDIO_TIME.getValue().intValue()) {

                launchPlanCache.updateRedis(tbLaunchPlan.getLaunchVideoId());

                //TODO
//                mqttService.pushVideoId("videoIdTopic",tbLaunchPlan.getLaunchVideoId());
                myRabbitmqTemplate.convertAndSend("fanoutExchange","",tbLaunchPlan.getLaunchVideoId());

            }

            //删除缓存
            redisManager.del((PRELOAD_VIDEO_ID_REDIS_KEY_PREFIX+tbLaunchPlan.getLaunchVideoId()).getBytes());
        }
        if(tbLaunchPlanList.get(0)!=null){
            // 记录操作日志
            operationLogService.writeOperationLog_42(tbLaunchPlanList.get(0).getLaunchPlanName());
        }
    }

    private List<LaunchPlanPageInfoResponseDTO.LaunchPlanInfo> batchTransformOperation(List<TbLaunchPlanOperationExt> tbLaunchPlanList) {

        List<LaunchPlanPageInfoResponseDTO.LaunchPlanInfo> launchPlanInfoList = new ArrayList<>();

        for(TbLaunchPlanOperationExt tbLaunchPlan:tbLaunchPlanList){

            LaunchPlanPageInfoResponseDTO.LaunchPlanInfo launchPlanInfo = new LaunchPlanPageInfoResponseDTO.LaunchPlanInfo();

            launchPlanInfo.setCreativeId(tbLaunchPlan.getCreativeId());

            launchPlanInfo.setCreativeName(tbLaunchPlan.getCreativeName());

            launchPlanInfo.setCreateDate(DateUtil.toFormatDateString(tbLaunchPlan.getGmtCreated(),DateUtil.DATE_FORMAT));

            launchPlanInfo.setInteractionTypeName(tbLaunchPlan.getInteractionName());

            launchPlanInfo.setLaunchStatus(tbLaunchPlan.getStatus());

            launchPlanInfo.setLaunchName(tbLaunchPlan.getLaunchPlanName());

            launchPlanInfo.setLaunchPlanId(tbLaunchPlan.getId());

            if(LaunchTimeTypeEnum.REAL_TIME.getValue().equals(tbLaunchPlan.getStatus())){
                if(StringUtils.isNotEmpty(tbLaunchPlan.getLaunchLenTime())){
                    launchPlanInfo.setLaunchLenTime(Integer.valueOf(tbLaunchPlan.getLaunchLenTime()));
                }
                if(LaunchStatusEnum.PASS.getValue().equals(tbLaunchPlan.getStatus())){
                    launchPlanInfo.setLaunchStartTime(DateUtil.formatDateDefault(tbLaunchPlan.getGmtModified()));
                }
            }

            launchPlanInfo.setLaunchTimeType(tbLaunchPlan.getLaunchTimeType());

            launchPlanInfoList.add(launchPlanInfo);
        }

        return launchPlanInfoList;

    }

    private List<LaunchPlanPageInfoResponseDTO.LaunchPlanInfo> batchTransform(List<TbLaunchPlanExt> tbLaunchPlanList) {

        List<LaunchPlanPageInfoResponseDTO.LaunchPlanInfo> launchPlanInfoList = new ArrayList<>();

        for(TbLaunchPlanExt tbLaunchPlan:tbLaunchPlanList){

            LaunchPlanPageInfoResponseDTO.LaunchPlanInfo launchPlanInfo = new LaunchPlanPageInfoResponseDTO.LaunchPlanInfo();

            launchPlanInfo.setCreativeId(tbLaunchPlan.getCreativeId());

            launchPlanInfo.setCreativeName(tbLaunchPlan.getCreativeName());

            launchPlanInfo.setCreateDate(DateUtil.toShortDateString(tbLaunchPlan.getGmtCreated()));

            launchPlanInfo.setInteractionTypeName(tbLaunchPlan.getInteractionName());

            launchPlanInfo.setLaunchStatus(tbLaunchPlan.getStatus());

            launchPlanInfo.setLaunchName(tbLaunchPlan.getLaunchPlanName());

            launchPlanInfo.setLaunchPlanId(tbLaunchPlan.getId());

            launchPlanInfoList.add(launchPlanInfo);
        }

        return launchPlanInfoList;

    }


    public void saveImage(Integer creativeId,TbCreativeImage creativeImage){

        TbCreative tbCreative = tbCreativeMapper.selectByPrimaryKey(creativeId);

        BeanUtils.copyEntityProperties(tbCreative,creativeImage);

        creativeImage.setId(null);

        creativeImage.setSourceId(creativeId);

        tbCreativeImageMapper.insertSelective(creativeImage);

    }

    @Override
    public String[] splitLaunchVideoIds(String launchVideoIds){
        if(StringUtils.isNotEmpty(launchVideoIds)){
            return launchVideoIds.split(",");
        }
        return new String[]{};
    }

}
