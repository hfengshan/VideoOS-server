package com.videojj.videoservice.service;

import com.alibaba.fastjson.JSONObject;
import com.videojj.videoservice.cache.LaunchPlanCache;
import com.videojj.videoservice.dao.TbCreativeMapper;
import com.videojj.videoservice.dao.TbLaunchPlanMapper;
import com.videojj.videoservice.dto.TbCreativeExtInfo;
import com.videojj.videoservice.entity.TbLaunchPlanApiInfoExt;
import com.videojj.videoservice.service.impl.ApiServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

/**
 * @Author @videopls.com
 * Created by  on 2018/9/27 下午4:29.
 * @Description:
 */
@RunWith(JUnit4.class)
public class ApiServiceTest {

    @InjectMocks
    private ApiServiceImpl apiService;

    @Mock
    private TbLaunchPlanMapper tbLaunchPlanMapper;

    @Mock
    private TbCreativeMapper tbCreativeMapper;

    @Mock
    private LaunchPlanCache launchPlanCache;

    /**
     *
     */
    @Before
    public void setUp() {

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

        jsonParam.put("materialContent", "materialContent");

        tbLaunchPlanApiInfoExt.setMaterialContent(jsonParam.toJSONString());

        tbLaunchPlanApiInfoExt.setTemplateFileName("fileName");

        Mockito.when(tbLaunchPlanMapper.selectByVideoId(any(Integer.class), any(String.class), any(Date.class), any(Byte.class))).thenReturn(launchPlanList);

        TbCreativeExtInfo tbCreativeExtInfo = new TbCreativeExtInfo();

        tbCreativeExtInfo.setTemplateFileName("templateFileName");

        tbCreativeExtInfo.setMaterialContent(jsonParam.toJSONString());

        Mockito.when(tbCreativeMapper.selectJoinTemplateByName(any(String.class))).thenReturn(tbCreativeExtInfo);

//      List<LaunchApiQueryInfoResponseDTO.LaunchInfo> launchInfoList  = new ArrayList<>();
//      Mockito.when(launchPlanCache.selectByVideoId(any(String.class))).thenReturn(launchInfoList);

        List<TbLaunchPlanApiInfoExt> _launchPlanList = new ArrayList<>();
        TbLaunchPlanApiInfoExt _tbLaunchPlanApiInfoExt = new TbLaunchPlanApiInfoExt();
        _launchPlanList.add(_tbLaunchPlanApiInfoExt);
        Mockito.when(tbLaunchPlanMapper.selectByVideoId(any(Integer.class), any(String.class), any(Date.class), any(Byte.class))).thenReturn(launchPlanList);
        Mockito.when(launchPlanCache.selectByVideoId(any(Integer.class),any(String.class),any(Date.class),any(Byte.class))).thenReturn(new ArrayList<>());

    }


    @Test
    public void queryInfoByVideoIdTest() {


        apiService.queryInfoByVideoId("videoId");
    }

    @Test
    public void queryInfoByCreativeNameTest() {

        apiService.queryInfoByCreativeName("creativeName");

    }
}
