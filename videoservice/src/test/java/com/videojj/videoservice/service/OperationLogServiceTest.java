package com.videojj.videoservice.service;

import com.videojj.videoservice.dao.TbOperationLogMapper;
import com.videojj.videoservice.dto.PageInfoDTO;
import com.videojj.videoservice.entity.TbOperationLog;
import com.videojj.videoservice.entity.TbOperationLogExt;
import com.videojj.videoservice.enums.OperationLogTypeEnum;
import com.videojj.videoservice.service.impl.OperationLogServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @Author @videopls.com
 * Created by  on 2018/9/27 下午4:29.
 * @Description:
 */
@RunWith(JUnit4.class)
public class OperationLogServiceTest {

    @InjectMocks
    private OperationLogServiceImpl operationLogService;

    @Mock
    private TbOperationLogMapper tbOperationLogMapper;

    /**
     *
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * 测试添加操作日志
     */
    @Test
    public void testAddOperationLog_1() {
        Mockito.when(tbOperationLogMapper.insertSelective(any(TbOperationLog.class))).thenReturn(1);
        operationLogService.addOperationLog("测试", OperationLogTypeEnum._1.getOperationType());

        verify(tbOperationLogMapper,times(1)).insertSelective(any(TbOperationLog.class));
    }

    /**
     * 测试分页查询为空的情况
     */
    @Test
    public void testQueryAllByPage_1() {
        List<TbOperationLogExt> tbOperationLogList = new ArrayList<>();

        Mockito.when(tbOperationLogMapper.selectByParamWithPage(any(PageInfoDTO.class))).thenReturn(tbOperationLogList);
        Mockito.when(tbOperationLogMapper.count()).thenReturn(0);
        Assert.assertEquals(operationLogService.queryAllByPage(1,10).getOperationLogList(),null);
    }

    /**
     * 测试分页查询正常的情况
     */
    @Test
    public void testQueryAllByPage_2() {
        List<TbOperationLogExt> tbOperationLogList = new ArrayList<>();
        tbOperationLogList.add(new TbOperationLogExt());
        tbOperationLogList.add(new TbOperationLogExt());
        tbOperationLogList.add(new TbOperationLogExt());
        Mockito.when(tbOperationLogMapper.selectByParamWithPage(any(PageInfoDTO.class))).thenReturn(tbOperationLogList);
        Mockito.when(tbOperationLogMapper.count()).thenReturn(3);
        Assert.assertEquals(operationLogService.queryAllByPage(1,10).getOperationLogList().size(),3);
    }


}
