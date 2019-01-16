package com.videojj.videoservice.service;

import com.videojj.videocommon.util.DateUtil;
import com.videojj.videoservice.dao.TbCreativeFileMapper;
import com.videojj.videoservice.dao.TbCreativeMapper;
import com.videojj.videoservice.dao.TbInteractionMapper;
import com.videojj.videoservice.dao.TbLaunchPlanMapper;
import com.videojj.videoservice.dto.AddLaunchPlanRequestDTO;
import com.videojj.videoservice.dto.LaunchPlanDetailInfoResponseDTO;
import com.videojj.videoservice.entity.*;
import com.videojj.videoservice.service.impl.CheckServiceImpl;
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
import static org.mockito.ArgumentMatchers.anyString;

/**
 * @Author @videopls.com
 * Created by  on 2018/10/23 下午5:06.
 * @Description:
 */
@RunWith(JUnit4.class)
public class CheckServiceImplTest {

    @InjectMocks
    private CheckServiceImpl checkService;

    @Mock
    private TbCreativeMapper tbCreativeMapper;

    @Mock
    private TbCreativeFileMapper tbCreativeFileMapper;

    @Mock
    private TbInteractionMapper tbInteractionMapper;

    @Mock
    private TbLaunchPlanMapper tbLaunchPlanMapper;

    @Mock
    private  LaunchPlanService launchPlanService;


    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        List<TbCreative> creativeList = new ArrayList<>();
        TbCreative creative = new TbCreative();
        creativeList.add(creative);
        Mockito.when(tbCreativeMapper.selectByParam(any(TbCreativeCriteria.class))).thenReturn(creativeList);

        List<TbCreativeFile> creativeFileList = new ArrayList<>();
        TbCreativeFile tbCreativeFile = new TbCreativeFile();
        tbCreativeFile.setCreativeId(1);
        creativeFileList.add(tbCreativeFile);
        Mockito.when(tbCreativeFileMapper.selectByCriteria(any(TbCreativeFileCriteria.class))).thenReturn(creativeFileList);

        List<TbInteraction> interactionList = new ArrayList<>();
        TbInteraction tbInteraction = new TbInteraction();
        tbInteraction.setId(1);
        interactionList.add(tbInteraction);
        Mockito.when(tbInteractionMapper.select(any(TbInteraction.class))).thenReturn(interactionList);

        TbLaunchPlan tbLaunchPlan = new TbLaunchPlan();
        tbLaunchPlan.setCreativeId(1);
        tbLaunchPlan.setLaunchTimeType((byte) 0);
        tbLaunchPlan.setLaunchTime("01:00");
        tbLaunchPlan.setLaunchDateStart(new Date());
        tbLaunchPlan.setLaunchLenTime("60");
        tbLaunchPlan.setLaunchDateEnd(DateUtil.addDays(new Date(), 1));
        Mockito.when(tbLaunchPlanMapper.selectByPrimaryKey(any(Integer.class))).thenReturn(tbLaunchPlan);


        List<TbLaunchPlan> tbLaunchPlanList = new ArrayList<>();
        tbLaunchPlanList.add(tbLaunchPlan);
        Mockito.when(tbCreativeMapper.updateByPrimaryKeySelective(any(TbCreative.class))).thenReturn(1);

    }

    @Test
    public void isInUseTemplateIdTest() {

        checkService.isInUseCreativeId(1);
    }

    @Test
    public void isInUseInteractionIdTest() {

        checkService.isInUseInteractionId(1);
    }

    @Test
    public void isInUseCreativeIdTest() {

        checkService.isInUseCreativeId(1);
    }

    @Test
    public void isInUseCreativeUrlTest() {

        checkService.isInUseCreativeUrl("url");
    }

    @Test
    public void isInUseTemplateNameTest() {

        checkService.isInUseTemplateName("template");
    }

    @Test
    public void updateUseStatusTest() {

        checkService.updateUseStatus(1);
    }

    @Test
    public void isInUseInteractionTypeNameTest() {

        checkService.isInUseInteractionTypeName("typeName");
    }

    @Test
    public void checkLaunchPlanTest() {

        List<TbLaunchPlan> _planList  = new ArrayList<>();
        TbLaunchPlan _tbLaunchPlan  = new TbLaunchPlan();
        _planList.add(_tbLaunchPlan);
        Mockito.when(tbLaunchPlanMapper.selectOnlineByLaunchVideoId(any(String.class))).thenReturn(new ArrayList<>());
        String[] str  =  new String[4];
        Mockito.when(launchPlanService.splitLaunchVideoIds(any(String.class))).thenReturn(str);

        AddLaunchPlanRequestDTO request = new AddLaunchPlanRequestDTO();
        request.setCreativeId(1);
        request.setUsername("username");
        List<List<String>> launchTime = new ArrayList<>();
        List<String> launcStrList = new ArrayList<>();
        launcStrList.add("01:00");
        launchTime.add(launcStrList);

        request.setLaunchTime(launchTime);
        request.setInteractionTypeId(1);
        request.setLaunchTimeType((byte) 0);
        request.setLaunchDateStart(DateUtil.toShortDateString(new Date()));
        request.setLaunchDateEnd(DateUtil.toShortDateString(DateUtil.addDays(new Date(), 1)));
        request.setLaunchLenTime("60");
        request.setLaunchPlanName("planName");
        request.setLaunchVideoId("1");

        checkService.checkLaunchPlan(request);
    }

}
