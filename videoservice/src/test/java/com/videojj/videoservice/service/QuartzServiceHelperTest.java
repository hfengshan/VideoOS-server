package com.videojj.videoservice.service;

import com.videojj.videocommon.util.DateUtil;
import com.videojj.videoservice.entity.TbLaunchPlan;
import com.videojj.videoservice.service.impl.QuartzServiceHelper;
import com.videojj.videoservice.task.HandleTimeAndSendEmqHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @Author @videopls.com
 * Created by  on 2018/9/27 下午4:29.
 * @Description:
 */
@RunWith(JUnit4.class)
public class QuartzServiceHelperTest {

    @InjectMocks
    private QuartzServiceHelper quartzServiceHelper;

    @Mock
    private HandleTimeAndSendEmqHelper helper;


    /**
     *
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * 测试日期区间不包含今天的情况
     */
    @Test
    public void testAddQuartz_1() throws ParseException {
        Date startDate = DateUtil.parseStdDateString("2018-10-29 10:10:10");
        Date endDate = DateUtil.parseStdDateString("2018-10-29 20:10:10");
        quartzServiceHelper.addQuartz(startDate,endDate,null,null);

        verify(helper,never()).handle(any(),any(),any());
    }

    /**
     * 测试日期区间包含今天的情况
     */
    @Test
    public void testAddQuartz_2() {
        Date now = DateUtil.getNow();
        Date startDate = DateUtil.addDays(now,-1);
        Date endDate = DateUtil.addDays(now,1);
        TbLaunchPlan tbLaunchPlan = new TbLaunchPlan();
        tbLaunchPlan.setLaunchVideoId("1");
        quartzServiceHelper.addQuartz(startDate,endDate,"[[\"10:10\",\"20:20\"]]",tbLaunchPlan);

        verify(helper,times(1)).handle(any(Date.class),any(List.class),any(TbLaunchPlan.class));
    }

    /**
     * 测试日期区间不包含今天的情况
     */
    @Test
    public void testRemoveQuartz_1() throws ParseException {
        Date startDate = DateUtil.parseStdDateString("2018-10-29 10:10:10");
        Date endDate = DateUtil.parseStdDateString("2018-10-29 20:10:10");
        quartzServiceHelper.removeQuartz(startDate,endDate,null,null);

        verify(helper,never()).stopPlan(any(),any(),any());
    }

    /**
     * 测试日期区间包含今天的情况
     */
    @Test
    public void testRemoveQuartz_2() {
        Date now = DateUtil.getNow();
        Date startDate = DateUtil.addDays(now,-1);
        Date endDate = DateUtil.addDays(now,1);
        TbLaunchPlan tbLaunchPlan = new TbLaunchPlan();
        tbLaunchPlan.setLaunchVideoId("1");
        quartzServiceHelper.removeQuartz(startDate,endDate,"[[\"10:10\",\"20:20\"]]",tbLaunchPlan);

        verify(helper,times(1)).stopPlan(any(Date.class),any(List.class),any(TbLaunchPlan.class));

    }


}
