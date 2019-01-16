package com.videojj.videoservice.service;

import com.videojj.videocommon.exception.ServiceException;
import com.videojj.videoservice.bo.RoleAndNodeBo;
import com.videojj.videoservice.dao.TbNodeMapper;
import com.videojj.videoservice.dao.TbRoleMapper;
import com.videojj.videoservice.entity.TbNode;
import com.videojj.videoservice.entity.TbNodeCriteria;
import com.videojj.videoservice.entity.TbRole;
import com.videojj.videoservice.service.impl.LoginServiceImpl;
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

/**
 * @Author @videopls.com
 * Created by  on 2018/9/27 下午4:29.
 * @Description:
 */
@RunWith(JUnit4.class)
public class LoginServiceTest {

    @InjectMocks
    private LoginServiceImpl loginService;

    @Mock
    private TbRoleMapper tbRoleMapper;

    @Mock
    private TbNodeMapper tbNodeMapper;

    /**
     *
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * 测试用户没有配置角色的情况
     */
    @Test
    public void testGetAuthInfoByUsername_1() {
        Mockito.when(tbRoleMapper.selectRoleInfoByUsername(any(String.class))).thenReturn(null);
        try {
            loginService.getAuthInfoByUsername("username");
        } catch (ServiceException e) {
            Assert.assertEquals(e.getMessage(), "该用户或者角色已经不存在");
        }

    }

    /**
     * 测试正常情况
     */
    @Test
    public void testGetAuthInfoByUsername_2() {
        TbRole tbRole = new TbRole();
        tbRole.setRoleName("roleName");
        tbRole.setId(1);
        Mockito.when(tbRoleMapper.selectRoleInfoByUsername(any(String.class))).thenReturn(tbRole);

        List<TbNode> leafNodeList = new ArrayList<>();
        TbNode tbNode1 = new TbNode();
        tbNode1.setParentPath("0");
        tbNode1.setId(2);
        tbNode1.setParentId(0);
        leafNodeList.add(tbNode1);
        TbNode tbNode2 = new TbNode();
        tbNode2.setParentPath("0");
        tbNode2.setId(3);
        tbNode2.setParentId(0);
        leafNodeList.add(tbNode2);
        Mockito.when(tbNodeMapper.selectByParam(any(TbNodeCriteria.class))).thenReturn(leafNodeList, new ArrayList<>());
        RoleAndNodeBo roleAndNodeBo = loginService.getAuthInfoByUsername("username");
        Assert.assertEquals(roleAndNodeBo.getNodeInfo().split("nodeId").length - 1, 4);
    }


}
