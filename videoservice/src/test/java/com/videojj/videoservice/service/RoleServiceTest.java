package com.videojj.videoservice.service;

import com.videojj.videocommon.constant.Constants;
import com.videojj.videocommon.exception.ServiceException;
import com.videojj.videoservice.dao.TbNodeMapper;
import com.videojj.videoservice.dao.TbRoleMapper;
import com.videojj.videoservice.dto.*;
import com.videojj.videoservice.entity.TbNode;
import com.videojj.videoservice.entity.TbRole;
import com.videojj.videoservice.service.impl.RoleServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(JUnit4.class)
public class RoleServiceTest {

    @InjectMocks
    private RoleServiceImpl roleServiceImpl;

    @Mock
    private TbRoleMapper tbRoleMapper;

    @Mock
    private TransactionTemplate transanctionTemplate;

    @Mock
    private TbNodeMapper tbNodeMapper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * 测试角色名称重复
     */
    @Test
    public void testAddRoleService_1(){
        Mockito.when(tbRoleMapper.selectByParam(any())).thenReturn(new ArrayList<TbRole>(){{
            add(new TbRole());
            add(new TbRole());
        }});

        AddRoleRequestDTO addRoleRequestDTO = new AddRoleRequestDTO();
        List<Integer> nodeList = new ArrayList<>();
        nodeList.add(1);
        addRoleRequestDTO.setRoleName("roleName1");
        addRoleRequestDTO.setNodeIdList(nodeList);
        try{
            roleServiceImpl.addRoleService(addRoleRequestDTO,"admin");
        }catch(ServiceException e){
            Assert.assertEquals(e.getMessage(),"角色名称重复，请更换角色名称");
        }
        verify(tbRoleMapper,times(0)).insertSelective(any());
    }

    /**
     * 测试正常逻辑
     */
    @Test
    public void testAddRoleService_2(){
        Mockito.when(tbRoleMapper.selectByParam(any())).thenReturn(null);

        AddRoleRequestDTO addRoleRequestDTO = new AddRoleRequestDTO();
        List<Integer> nodeList = new ArrayList<>();
        nodeList.add(1);
        addRoleRequestDTO.setRoleName("roleName1");
        addRoleRequestDTO.setNodeIdList(nodeList);
        roleServiceImpl.addRoleService(addRoleRequestDTO,"admin");
        verify(tbRoleMapper,times(1)).insertSelective(any());
    }

    /**
     * 测试角色名称重复
     */
    @Test
    public void testUpdateRoleService_1(){
        TbRole tbRole = new TbRole();
        tbRole.setId(2);
        Mockito.when(tbRoleMapper.selectByParam(any())).thenReturn(new ArrayList<TbRole>(){{
            add(tbRole);
        }});

        UpdateRoleRequestDTO updateRoleRequestDTO = new UpdateRoleRequestDTO();
        List<Integer> nodeList = new ArrayList<>();
        nodeList.add(1);
        updateRoleRequestDTO.setRoleName("roleName1");
        updateRoleRequestDTO.setNodeIdList(nodeList);
        updateRoleRequestDTO.setRoleId(1);
        try{
            roleServiceImpl.updateRoleService(updateRoleRequestDTO,"admin");
        }catch(ServiceException e){
            Assert.assertEquals(e.getMessage(),"角色名称重复，请更换角色名称");
        }

        verify(transanctionTemplate,times(0)).execute(any());
    }

    /**
     * 测试正常逻辑
     */
    @Test
    public void testUpdateRoleService_2(){
        TbRole tbRole = new TbRole();
        tbRole.setId(1);
        Mockito.when(tbRoleMapper.selectByParam(any())).thenReturn(
                new ArrayList<TbRole>(){{
                    add(tbRole);
                }}
        );
        TbNode tbNode = new TbNode();
        tbNode.setDesc("desc");
        Mockito.when(tbNodeMapper.selectByParam(any())).thenReturn(
                new ArrayList<TbNode>(){{
                    add(tbNode);
                }}
        );

        UpdateRoleRequestDTO addRoleRequestDTO = new UpdateRoleRequestDTO();
        List<Integer> nodeList = new ArrayList<>();
        nodeList.add(1);
        addRoleRequestDTO.setRoleName("roleName1");
        addRoleRequestDTO.setNodeIdList(nodeList);
        addRoleRequestDTO.setRoleId(1);
        roleServiceImpl.updateRoleService(addRoleRequestDTO,"admin");
        verify(transanctionTemplate,times(1)).execute(any());
    }

    /**
     * 测试正常逻辑
     */
    @Test
    public void testDeleteRoleService_1(){
        DeleteRoleRequestDTO updateRoleRequestDTO = new DeleteRoleRequestDTO();
        updateRoleRequestDTO.setRoleId(1);
        roleServiceImpl.deleteRoleService(updateRoleRequestDTO,"admin");

        verify(transanctionTemplate,times(1)).execute(any());
    }

    /**
     * 测试正常逻辑
     */
    @Test
    public void testQueryPageInfoByParam_1(){
        QueryRoleRequestDTO queryRoleRequestDTO = new QueryRoleRequestDTO();
        queryRoleRequestDTO.setCurrentPage(1);
        queryRoleRequestDTO.setPageSize(10);
        Mockito.when(tbRoleMapper.countByParam(any())).thenReturn(11);
        TbRole tbRole = new TbRole();
        tbRole.setId(1);
        Mockito.when(tbRoleMapper.selectByParamWithPage(any(),any())).thenReturn(
                new ArrayList<TbRole>(){{
                    add(tbRole);
                }}
        );
        RolePageInfoResponseDTO responseDTO = roleServiceImpl.queryPageInfoByParam(queryRoleRequestDTO);
        Assert.assertEquals(responseDTO.getTotalPage(),new Integer(2));
    }

    /**
     * 测试正常逻辑
     */
    @Test
    public void testQueryAuthInfoByParam_1(){
        TbRole tbRole = new TbRole();
        tbRole.setRoleName("roleName");
        tbRole.setAuths("1,2,3");
        Mockito.when(tbRoleMapper.selectByPrimaryKey(any())).thenReturn(tbRole);

        RoleOwnAuthInfoResponseDTO responseDTO = roleServiceImpl.queryAuthInfoByParam(1);
        Assert.assertEquals(responseDTO.getResCode(), Constants.SUCESSCODE);
    }

    /**
     * 测试正常逻辑
     */
    @Test
    public void testQueryAll_1(){
        Mockito.when(tbRoleMapper.selectByParam(any())).thenReturn(new ArrayList<>());

        AllRoleInfoResponseDTO responseDTO = roleServiceImpl.queryAll();
        Assert.assertEquals(responseDTO.getResCode(), Constants.SUCESSCODE);
    }



}
