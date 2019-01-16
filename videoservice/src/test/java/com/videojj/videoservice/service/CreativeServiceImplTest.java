package com.videojj.videoservice.service;

import com.alibaba.fastjson.JSONObject;
import com.videojj.videoservice.dao.TbCreativeFileMapper;
import com.videojj.videoservice.dao.TbCreativeMapper;
import com.videojj.videoservice.dto.AddCreativeRequestDTO;
import com.videojj.videoservice.dto.TbCreativeExtInfo;
import com.videojj.videoservice.entity.*;
import com.videojj.videoservice.service.impl.ApiServiceImpl;
import com.videojj.videoservice.service.impl.CreativeServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

/**
 * @Author @videopls.com
 * Created by  on 2018/10/23 上午11:12.
 * @Description:
 */
@RunWith(JUnit4.class)
public class CreativeServiceImplTest {

    @InjectMocks
    private CreativeServiceImpl creativeService;

    @Mock
    private TbCreativeMapper tbCreativeMapper;

    @Mock
    private TbCreativeFileMapper tbCreativeFileMapper;

    @Mock
    private TransactionTemplate transactionTemplate;
    /**
     *
     */
    @Before
    public void setUp(){

        MockitoAnnotations.initMocks(this);

        List<TbCreative> creativeList = new ArrayList<>();

        TbCreative tbCreative = new TbCreative();

        tbCreative.setTemplateName("templateName");
        tbCreative.setInteractionName("interationName");
        tbCreative.setMaterialContent("materialContent");
        tbCreative.setInteractionId(1);
        tbCreative.setMaterialName("materialName");
        tbCreative.setStatus((byte) 1);
        tbCreative.setTemplateId(1);
        creativeList.add(tbCreative);

        Mockito.when(tbCreativeMapper.selectByParam(any(TbCreativeCriteria.class))).thenReturn(creativeList);
        Mockito.when(tbCreativeMapper.insertSelective(any(TbCreative.class))).thenReturn(1);
        Mockito.when(tbCreativeFileMapper.updateByCriteriaSelective(any(TbCreativeFile.class),any(TbCreativeFileCriteria.class))).thenReturn(1);
        Mockito.when(transactionTemplate.execute(any(TransactionCallback.class))).thenReturn(null);
    }

    @Test
    public void addUploadTest(){

        Mockito.when(tbCreativeMapper.selectByParam(any(TbCreativeCriteria.class))).thenReturn(new ArrayList<>());
        AddCreativeRequestDTO request = new AddCreativeRequestDTO();
        request.setCreativeName("creativeName");
        request.setUsername("username");
        request.setCreativeContent("creativeContent");
        List<Integer> idList = new ArrayList<>();
        idList.add(1);
        request.setCreativeIdList(idList);
        request.setInteractionTemplateId(1);
        request.setInteractionTemplateName("tempalteName");
        request.setInteractionTypeId(1);
        request.setInteractionTypeName("typeName");
        creativeService.addCreative(request);
    }

}
