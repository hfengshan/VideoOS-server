package com.videojj.videoservice.service;

import com.alibaba.fastjson.JSONObject;
import com.videojj.videocommon.constant.Constants;
import com.videojj.videocommon.exception.ServiceException;
import com.videojj.videocommon.util.DateUtil;
import com.videojj.videoservice.cache.LaunchPlanCache;
import com.videojj.videoservice.dao.TbCreativeImageMapper;
import com.videojj.videoservice.dao.TbCreativeMapper;
import com.videojj.videoservice.dao.TbLaunchPlanMapper;
import com.videojj.videoservice.dao.TbLaunchPlanOperationMapper;
import com.videojj.videoservice.dto.*;
import com.videojj.videoservice.emqttd.MqttService;
import com.videojj.videoservice.entity.*;
import com.videojj.videoservice.enums.LaunchStatusEnum;
import com.videojj.videoservice.enums.LaunchTimeTypeEnum;
import com.videojj.videoservice.handler.SendEmqService;
import com.videojj.videoservice.service.impl.LaunchPlanServiceImpl;
import com.videojj.videoservice.service.impl.QuartzServiceHelper;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.apache.commons.lang.time.DateUtils;
import org.atteo.evo.inflector.English;
import org.crazycake.shiro.RedisManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;

/**
 * Created by  on 2018/10/23 下午4:29.
 */
@RunWith(JUnit4.class)
public class LaunchPlanServiceTest extends BaseTest{

    @InjectMocks
    private LaunchPlanServiceImpl launchPlanService;

    @Mock
    private TbLaunchPlanMapper tbLaunchPlanMapper;

    @Mock
    private TbCreativeMapper tbCreativeMapper;

    @Mock
    private TransactionTemplate transactionTemplate;

    @Mock
    private CheckService checkService;

    @Mock
    private TbCreativeImageMapper tbCreativeImageMapper;

    @Mock
    private MqttService mqttService;

    @Mock
    protected SendEmqService sendEmqService;

    @Mock
    private QuartzServiceHelper quartzServiceHelper;

    @Mock
    private OperationLogService operationLogService;

    @Mock
    private TbLaunchPlanOperationMapper tbLaunchPlanOperationMapper;

    @Mock
    private LaunchPlanCache launchPlanCache;

    @Mock
    private RabbitTemplate myRabbitmqTemplate;

    @Mock
    private RedisManager redisManager;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);

        List<TbLaunchPlanApiInfoExt> launchPlanList = new ArrayList<>();
        TbLaunchPlanApiInfoExt tbLaunchPlanApiInfoExt = new TbLaunchPlanApiInfoExt();
        launchPlanList.add(tbLaunchPlanApiInfoExt);
        tbLaunchPlanApiInfoExt.setLaunchLenTime("30");
        tbLaunchPlanApiInfoExt.setLaunchVideoId("videoid");
        tbLaunchPlanApiInfoExt.setLaunchDateEnd(new Date());
        tbLaunchPlanApiInfoExt.setLaunchDateStart(new Date());
        tbLaunchPlanApiInfoExt.setLaunchTime("00:01");
        tbLaunchPlanApiInfoExt.setLaunchTimeType((byte) 1);
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("materialContent","materialContent");
        tbLaunchPlanApiInfoExt.setMaterialContent(jsonParam.toJSONString());
        tbLaunchPlanApiInfoExt.setTemplateFileName("fileName");

        TbCreativeImage tbCreativeImage = new TbCreativeImage();
        tbCreativeImage.setSourceId(1);
        tbCreativeImage.setInteractionId(1);
        tbCreativeImage.setInteractionName("name");
        tbCreativeImage.setTemplateId(1);
        tbCreativeImage.setTemplateName("name");
        tbCreativeImage.setExtraInfo("extrainfo");
        tbCreativeImage.setCreator("admin");
        tbCreativeImage.setGmtCreated(new Date());
        tbCreativeImage.setGmtModified(new Date());
        tbCreativeImage.setIsDeleted("N");
        tbCreativeImage.setMaterialContent("MaterialContent");
        tbCreativeImage.setHotSpotNum(1);
//        图片插入操作
//        Mockito.when(tbLaunchPlanOperationMapper.insert(any())).thenReturn()


        TbCreative tbCreative  = new TbCreative();
        Mockito.when(tbCreativeMapper.selectByPrimaryKey(any(Integer.class))).thenReturn(null);

        Mockito.doNothing().when(quartzServiceHelper).addQuartz(any(),any(),any(),any());
        Mockito.doNothing().when(quartzServiceHelper).removeQuartz(any(Date.class),any(Date.class),any(String.class),any(TbLaunchPlan.class));
//        Mockito.when(tbLaunchPlanMapper.selectByParam(any(TbLaunchPlanCriteria.class))).thenReturn(null);
        Mockito.when(tbLaunchPlanMapper.selectByVideoId(any(),any( ),any( ),any( ))).thenReturn(launchPlanList);
        Mockito.when(transactionTemplate.execute(any(TransactionCallback.class))).thenReturn(null);
        Mockito.doNothing().when(mqttService).push(any(),any());


        List<TbLaunchPlan> tbLaunchPlanList = new ArrayList<>();
        TbLaunchPlan tbLaunchPlan = new TbLaunchPlan();
        tbLaunchPlan.setId(1);
        tbLaunchPlan.setInteractionId(1);
        tbLaunchPlan.setLaunchPlanName("name");
        tbLaunchPlan.setCreativeId(1);
        tbLaunchPlan.setCreativeImageId(1);
        tbLaunchPlan.setLaunchVideoId("id");
        tbLaunchPlan.setLaunchTimeType((byte) 0);
        tbLaunchPlan.setLaunchDateStart(new Date());
        tbLaunchPlan.setLaunchDateEnd(new Date());


        tbLaunchPlanList.add(tbLaunchPlan);

        Mockito.when(tbLaunchPlanMapper.selectByOperationId(any())).thenReturn(tbLaunchPlanList);
    }

    /**
     * 测试名称重复
     */

    @Test(expected=NullPointerException.class)
    public void testAddLaunchPlanService_1() {
       /*Mockito.when(tbLaunchPlanMapper.selectByParam(any(TbLaunchPlanCriteria.class))).thenReturn(
                new ArrayList<TbLaunchPlan>(){{
                    add(new TbLaunchPlan());
                }});*/

        AddLaunchPlanRequestDTO request = new AddLaunchPlanRequestDTO();

        try {

            TbLaunchPlanOperation tbLaunchPlanOperation = new TbLaunchPlanOperation();
            tbLaunchPlanOperation.setLaunchPlanName("LaunchPlanName");
            tbLaunchPlanOperation.setId(1);

            request.setLaunchPlanName("launchPlanName");
            request.setLaunchDateStart(DateUtil.toShortDateString(new Date()));
            request.setLaunchDateEnd(DateUtil.toShortDateString(new Date()));
            request.setLaunchTimeType(LaunchTimeTypeEnum.VIDIO_TIME.getValue());

            Mockito.when(tbLaunchPlanOperationMapper.selectByPrimaryKey(any())).thenReturn(tbLaunchPlanOperation);
            Mockito.when(tbLaunchPlanMapper.selectByOperationId(any(Integer.class))).thenReturn(new ArrayList<>());
            Mockito.when(tbLaunchPlanOperationMapper.select(any(TbLaunchPlanOperation.class))).thenReturn(null);
            Mockito.when(launchPlanCache.updateRedis(any())).thenReturn(null);

            launchPlanService.addLaunchPlanService(request);

        }catch(ServiceException e){
            Assert.assertEquals("名称重复，重新命名投放计划名称",e.getMessage());
            return;
        }
        throw new RuntimeException("测试未通过，没有抛出异常!");

    }

    /**
     * 投放日期格式异常 - 投放开始时间格式错误
     */
    @Test
    public void testAddLaunchPlanService_2() {
        AddLaunchPlanRequestDTO request = new AddLaunchPlanRequestDTO();
        request.setLaunchPlanName("launchPlanName");
        request.setLaunchDateStart("xxx");

        try {
            launchPlanService.addLaunchPlanService(request);

        }catch(ServiceException e){
            Assert.assertEquals("投放日期格式有问题",e.getMessage());
            return;
        }

        throw new RuntimeException("测试未通过，没有抛出异常!");

    }

    /**
     * 投放日期格式异常 - 投放结束时间格式错误
     */
    @Test
    public void testAddLaunchPlanService_3() {
        AddLaunchPlanRequestDTO request = new AddLaunchPlanRequestDTO();
        request.setLaunchPlanName("launchPlanName");
        request.setLaunchDateEnd("xxx");

        try {
            launchPlanService.addLaunchPlanService(request);
        }catch(ServiceException e){
            Assert.assertEquals("投放日期格式有问题",e.getMessage());
            return;
        }

        throw new RuntimeException("测试未通过，没有抛出异常!");

    }

    /**
     * 测试视频时间
     */
    @Test(expected=NullPointerException.class)
    public void testAddLaunchPlanService_4() {
        AddLaunchPlanRequestDTO request = new AddLaunchPlanRequestDTO();
        request.setLaunchPlanName("launchPlanName");
        request.setLaunchTimeType(LaunchTimeTypeEnum.VIDIO_TIME.getValue());
        request.setLaunchDateEnd("2019-01-01 15:03:53");

        TbLaunchPlanOperation tbLaunchPlanOperation = new TbLaunchPlanOperation();
        tbLaunchPlanOperation.setId(1);
        Mockito.when(tbLaunchPlanOperationMapper.selectByPrimaryKey(any(Integer.class))).thenReturn(null);


        launchPlanService.addLaunchPlanService(request);
    }

    /**
     * 测试即时投放
     */
    @Test
    public void testAddLaunchPlanService_5() {
        AddLaunchPlanRequestDTO request = new AddLaunchPlanRequestDTO();
        request.setLaunchPlanName("launchPlanName");
        request.setLaunchVideoId("LaunchVideoId");
        request.setLaunchTimeType(LaunchTimeTypeEnum.REAL_TIME.getValue());
        request.setLaunchDateEnd(DateUtil.toShortDateString(new Date()));
        launchPlanService.addLaunchPlanService(request);
    }
    /**
     * 测试北京时间
     */
    @Test
    public void testAddLaunchPlanService_6() {

        AddLaunchPlanRequestDTO request = new AddLaunchPlanRequestDTO();
        request.setLaunchPlanName("launchPlanName");
        request.setLaunchVideoId("LaunchVideoId");
        request.setLaunchTimeType(LaunchTimeTypeEnum.BJ_TIME.getValue());
        request.setLaunchDateStart(DateUtil.toShortDateString(new Date()));
        List<List<String>> launchTime = new ArrayList<>();
        List<String> launcStrList = new ArrayList<>();
        launcStrList.add("01:00");
        launchTime.add(launcStrList);
        request.setLaunchTime(launchTime);
        request.setLaunchLenTime("60");

        TbLaunchPlanOperation tbLaunchPlanOperation = new TbLaunchPlanOperation();
        tbLaunchPlanOperation.setId(1);
        tbLaunchPlanOperation.setLaunchTimeType((byte) 0);

        tbLaunchPlanOperation.setLaunchTime("1");
        Mockito.when(tbLaunchPlanOperationMapper.selectByPrimaryKey(any())).thenReturn(tbLaunchPlanOperation);
        Mockito.when(tbLaunchPlanMapper.selectByOperationId(any())).thenReturn(new ArrayList<>());

        launchPlanService.addLaunchPlanService(request);
    }

    /**
     * 测试北京时间
     */
    @Test
    public void testUpdateLaunchPlanService_1() throws Exception{

        TbLaunchPlan tbLaunchPlan = new TbLaunchPlan();
        tbLaunchPlan.setLaunchPlanName("launchPlanName");
        tbLaunchPlan.setLaunchTimeType(LaunchTimeTypeEnum.BJ_TIME.getValue());
        Mockito.when(tbLaunchPlanMapper.selectByPrimaryKey(any())).thenReturn(tbLaunchPlan);

        UpdateLaunchPlanRequestDTO request = new UpdateLaunchPlanRequestDTO();
        request.setLaunchPlanName("launchPlanName");
        request.setLaunchVideoId("LaunchVideoId");
        request.setLaunchTimeType(LaunchTimeTypeEnum.BJ_TIME.getValue());

        launchPlanService.updateLaunchPlanService(request);
    }

    /**
     * 测试即时投放
     */
    @Test
    public void testUpdateLaunchPlanService_2()  throws Exception{

        TbLaunchPlan tbLaunchPlan = new TbLaunchPlan();
        tbLaunchPlan.setLaunchPlanName("launchPlanName");
        tbLaunchPlan.setLaunchTimeType(LaunchTimeTypeEnum.REAL_TIME.getValue());
        Mockito.when(tbLaunchPlanMapper.selectByPrimaryKey(any())).thenReturn(tbLaunchPlan);

        UpdateLaunchPlanRequestDTO request = new UpdateLaunchPlanRequestDTO();
        request.setLaunchPlanName("launchPlanName");
        request.setLaunchVideoId("LaunchVideoId");
        request.setLaunchTimeType(LaunchTimeTypeEnum.REAL_TIME.getValue());

        launchPlanService.updateLaunchPlanService(request);
    }

    /**
     * 测试视频时间
     */
    @Test
    public void testUpdateLaunchPlanService_3() throws Exception{

        TbLaunchPlan tbLaunchPlan = new TbLaunchPlan();
        tbLaunchPlan.setLaunchPlanName("launchPlanName");
        tbLaunchPlan.setLaunchTimeType(LaunchTimeTypeEnum.VIDIO_TIME.getValue());
        Mockito.when(tbLaunchPlanMapper.selectByPrimaryKey(any())).thenReturn(tbLaunchPlan);

        UpdateLaunchPlanRequestDTO request = new UpdateLaunchPlanRequestDTO();
        request.setLaunchPlanName("launchPlanName");
        request.setLaunchVideoId("LaunchVideoId");
        request.setLaunchTimeType(LaunchTimeTypeEnum.VIDIO_TIME.getValue());

        launchPlanService.updateLaunchPlanService(request);
    }

    /**
     * 测试分页查询
     */
    @Test
    public void testQueryPageInfoByParam_1() {

       /* List<TbLaunchPlanExt> tbLaunchPlanList = new ArrayList<>();
        TbLaunchPlanExt tbLaunchPlan = new TbLaunchPlanExt();
        tbLaunchPlan.setLaunchPlanName("launchPlanName");
        tbLaunchPlanList.add(tbLaunchPlan);
        Mockito.when(tbLaunchPlanMapper.selectByParamWithPage(any())).thenReturn(tbLaunchPlanList);
        Mockito.when(tbLaunchPlanMapper.countByParam((any()))).thenReturn(11);

        QueryLaunchPlanRequestDTO request = new QueryLaunchPlanRequestDTO();
        request.setPageSize(10);
        request.setCurrentPage(1);
        request.setInteractionTypeId(1);

        LaunchPlanPageInfoResponseDTO responseDTO = launchPlanService.queryPageInfo(request);
        Assert.assertEquals(responseDTO.getTotalPage(),new Integer(2));
        Assert.assertEquals(responseDTO.getTotalRecord(),new Integer(11));*/
    }

    /**
     * 测试审核通过
     */
    @Test
    public void testAuditLaunchPlanService_1() {
        mockTransactionTemplate(transactionTemplate);
        AuditLaunchPlanRequestDTO request = new AuditLaunchPlanRequestDTO();
        request.setAuditStatus(LaunchStatusEnum.PASS.getValue());
        launchPlanService.auditLaunchPlanService(request);
    }

    /**
     * 测试审核未通过
     */
    @Test
    public void testAuditLaunchPlanService_2() {
        mockTransactionTemplate(transactionTemplate);
        AuditLaunchPlanRequestDTO request = new AuditLaunchPlanRequestDTO();
        request.setAuditStatus(LaunchStatusEnum.NOT_PASS.getValue());
        launchPlanService.auditLaunchPlanService(request);
    }

    /**
     * 测试待审核
     */
    @Test
    public void testAuditLaunchPlanService_3() {
        mockTransactionTemplate(transactionTemplate);
        AuditLaunchPlanRequestDTO request = new AuditLaunchPlanRequestDTO();
        request.setAuditStatus(LaunchStatusEnum.WAIT_PASS.getValue());
        launchPlanService.auditLaunchPlanService(request);
    }

    /**
     * 测试关闭
     */
    @Test
    public void testAuditLaunchPlanService_4() {
        mockTransactionTemplate(transactionTemplate);
        AuditLaunchPlanRequestDTO request = new AuditLaunchPlanRequestDTO();
        request.setAuditStatus(LaunchStatusEnum.CLOSE.getValue());
        launchPlanService.auditLaunchPlanService(request);
    }



    /**
     * 测试北京时间
     */
    @Test
    public void testDeleteLaunchPlanService_1() {
        TbLaunchPlan tbLaunchPlan = new TbLaunchPlan();
        tbLaunchPlan.setLaunchTimeType(LaunchTimeTypeEnum.BJ_TIME.getValue());
        Mockito.when(tbLaunchPlanMapper.selectByPrimaryKey(any())).thenReturn(tbLaunchPlan);
        DeleteLaunchPlanRequestDTO request = new DeleteLaunchPlanRequestDTO();
        launchPlanService.deleteLaunchPlanService(request);
    }

    /**
     * 测试实时投放
     */
    @Test
    public void testDeleteLaunchPlanService_2() {
        TbLaunchPlan tbLaunchPlan = new TbLaunchPlan();
        tbLaunchPlan.setLaunchTimeType(LaunchTimeTypeEnum.REAL_TIME.getValue());
        Mockito.when(tbLaunchPlanMapper.selectByPrimaryKey(any())).thenReturn(tbLaunchPlan);
        DeleteLaunchPlanRequestDTO request = new DeleteLaunchPlanRequestDTO();
        launchPlanService.deleteLaunchPlanService(request);
    }

    /**
     * 测试视频时间
     */
    @Test
    public void testDeleteLaunchPlanService_3() {
        TbLaunchPlan tbLaunchPlan = new TbLaunchPlan();
        tbLaunchPlan.setLaunchTimeType(LaunchTimeTypeEnum.VIDIO_TIME.getValue());
        Mockito.when(tbLaunchPlanMapper.selectByPrimaryKey(any())).thenReturn(tbLaunchPlan);
        DeleteLaunchPlanRequestDTO request = new DeleteLaunchPlanRequestDTO();
        launchPlanService.deleteLaunchPlanService(request);
    }

    /**
     * 测试投放信息的详细信息
     */
//    @Test
    @Test(expected=NullPointerException.class)
    public void testQueryDetailInfoById_1() {
        TbLaunchPlanExt tbLaunchPlan = new TbLaunchPlanExt();

        List<TbCreativeImage> creativeImage = new ArrayList<>();
        TbCreativeImage tbCreativeImage = new TbCreativeImage();
        tbCreativeImage.setHotSpotNum(1);
        tbCreativeImage.setMaterialContent("MaterialContent");
        tbCreativeImage.setSourceId(1);
        tbCreativeImage.setIsDeleted("N");
        creativeImage.add(tbCreativeImage);

        Mockito.when(tbLaunchPlanMapper.selectDetailInfoById(any())).thenReturn(tbLaunchPlan);
        Mockito.when(tbLaunchPlanOperationMapper.selectDetailInfoById(any(Integer.class))).thenReturn(null);
        Mockito.when(tbCreativeImageMapper.selectByCriteria(any(TbCreativeImageCriteria.class))).thenReturn(creativeImage);

        tbLaunchPlan.setCreativeId(1);
        tbLaunchPlan.setLaunchTime("[]");
        tbLaunchPlan.setIsDeleted("N");
        tbLaunchPlan.setStatus((byte) 1);
        LaunchPlanDetailInfoResponseDTO responseDTO = launchPlanService.queryDetailInfoById(null);
        Mockito.when(launchPlanService.queryDetailInfoById(any())).thenReturn(null);
        Assert.assertEquals(responseDTO.getResCode(), Constants.SUCESSCODE);
    }

    /**
     * 测试北京时间
     */
    @Test
    public void testOfflineLaunchPlanService_1() {
        TbLaunchPlan tbLaunchPlan = new TbLaunchPlan();
        tbLaunchPlan.setLaunchTimeType(LaunchTimeTypeEnum.BJ_TIME.getValue());
        Mockito.when(tbLaunchPlanMapper.selectByPrimaryKey(any())).thenReturn(tbLaunchPlan);
        DeleteLaunchPlanRequestDTO requsest = new DeleteLaunchPlanRequestDTO();
        launchPlanService.offlineLaunchPlanService(requsest);
    }

    /**
     * 测试实时投放
     */
    @Test
    public void testOfflineLaunchPlanService_2() {
        TbLaunchPlan tbLaunchPlan = new TbLaunchPlan();
        tbLaunchPlan.setLaunchTimeType(LaunchTimeTypeEnum.REAL_TIME.getValue());
        Mockito.when(tbLaunchPlanMapper.selectByPrimaryKey(any())).thenReturn(tbLaunchPlan);
        DeleteLaunchPlanRequestDTO requsest = new DeleteLaunchPlanRequestDTO();
        launchPlanService.offlineLaunchPlanService(requsest);
    }

    /**
     * 测试视频时间
     */
    @Test
    public void testOfflineLaunchPlanService_3() {
        TbLaunchPlan tbLaunchPlan = new TbLaunchPlan();
        tbLaunchPlan.setLaunchTimeType(LaunchTimeTypeEnum.VIDIO_TIME.getValue());
        Mockito.when(tbLaunchPlanMapper.selectByPrimaryKey(any())).thenReturn(tbLaunchPlan);
        DeleteLaunchPlanRequestDTO requsest = new DeleteLaunchPlanRequestDTO();
        TbLaunchPlan _tbLaunchPlan = new TbLaunchPlan();
        _tbLaunchPlan.setLaunchVideoId("1");
        Mockito.when(launchPlanCache.updateRedis(any())).thenReturn(null);
        launchPlanService.offlineLaunchPlanService(requsest);
    }
}
