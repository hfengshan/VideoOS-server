package com.videojj.videoservice.service;

import com.videojj.videocommon.exception.ServiceException;
import com.videojj.videoservice.dao.TbUserMapper;
import com.videojj.videoservice.dto.*;
import com.videojj.videoservice.entity.TbUser;
import com.videojj.videoservice.entity.TbUserExt;
import com.videojj.videoservice.entity.UserAndRoleInfo;
import com.videojj.videoservice.service.impl.UserServiceImpl;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(JUnit4.class)
public class UserServiceTest extends BaseTest {

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Mock
    private TbUserMapper tbUserMapper;

    @Mock
    private TransactionTemplate transactionTemplate;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * 测试 用户名称重复，请更换用户名称 的情况
     */
    @Test
    public void testAddUserService_1() {
        Mockito.when(tbUserMapper.selectByParam(any())).thenReturn(new ArrayList<TbUser>() {{
            add(new TbUser());
            add(new TbUser());
        }});
        AddUserRequestDTO addUserRequestDTO = new AddUserRequestDTO();
        addUserRequestDTO.setUsername("admin");
        try {
            userServiceImpl.addUserService(addUserRequestDTO, "admin");
        } catch (ServiceException e) {
            Assert.assertEquals(e.getMessage(), "用户名称重复，请更换用户名称");
        }
        verify(tbUserMapper, times(0)).insertSelective(any());
    }

    /**
     * 测试正常情况
     */
    @Test
    public void testAddUserService_2() {
        Mockito.when(tbUserMapper.selectByParam(any())).thenReturn(new ArrayList<TbUser>() {{
        }});
        AddUserRequestDTO addUserRequestDTO = new AddUserRequestDTO();
        addUserRequestDTO.setUsername("admin");
        addUserRequestDTO.setPassword("admin");
        userServiceImpl.addUserService(addUserRequestDTO, "admin");
        verify(tbUserMapper, times(1)).insertSelective(any());
    }

    /**
     * 测试 用户名称重复，请更换用户名称 的情况
     */
    @Test
    public void testUpdateUserService_1() {
        TbUser tbUser = new TbUser();
        tbUser.setUsername("normal");
        //模拟修改的用户 原用户名
        Mockito.when(tbUserMapper.selectByPrimaryKey(any())).thenReturn(tbUser);
        //模拟修改后的用户名已经存在的情况
        Mockito.when(tbUserMapper.selectByParam(any())).thenReturn(new ArrayList<TbUser>() {{
            add(new TbUser());
        }});
        UpdateUserRequestDTO requestDTO = new UpdateUserRequestDTO();
        requestDTO.setUsername("admin");
        try {
            userServiceImpl.updateUserService(requestDTO, "admin");
        } catch (ServiceException e) {
            Assert.assertEquals(e.getMessage(), "用户名称重复，请更换用户名称");
        }
        verify(transactionTemplate, times(0)).execute(any());
    }

    /**
     * 测试正常情况
     */
    @Test
    public void testUpdateUserService_2() {
        TbUser tbUser = new TbUser();
        tbUser.setUsername("admin");
        //模拟修改的用户 原用户名
        Mockito.when(tbUserMapper.selectByPrimaryKey(any())).thenReturn(tbUser);
        UpdateUserRequestDTO requestDTO = new UpdateUserRequestDTO();
        requestDTO.setUsername("admin");
        userServiceImpl.updateUserService(requestDTO, "admin");
        verify(transactionTemplate, times(1)).execute(any());
    }

    /**
     * 测试正常逻辑
     */
    @Test
    public void testQueryPageInfoByParam_1() {
        QueryUserRequestDTO requestDTO = new QueryUserRequestDTO();
        requestDTO.setCurrentPage(1);
        requestDTO.setPageSize(10);
        Mockito.when(tbUserMapper.countByParam(any())).thenReturn(11);
        TbUserExt tbUserExt = new TbUserExt();
        tbUserExt.setId(1);
        Mockito.when(tbUserMapper.selectByParamWithPage(any())).thenReturn(
                new ArrayList<TbUserExt>() {{
                    add(tbUserExt);
                }}
        );
        UserPageInfoResponseDTO responseDTO = userServiceImpl.queryPageInfoByParam(requestDTO);
        Assert.assertEquals(responseDTO.getTotalPage(), new Integer(2));
    }

    /**
     * 测试正常逻辑
     */
    @Test
    public void testDeleteUserService_1() {
        DeleteUserRequestDTO requestDTO = new DeleteUserRequestDTO();
        userServiceImpl.deleteUserService(requestDTO, "admin");
        verify(transactionTemplate, times(1)).execute(any());
    }

    /**
     * 测试返回null
     */
    @Test
    public void testGetAuthByUsername_1() {
        Mockito.when(tbUserMapper.selectRoleInfoByUsername(any())).thenReturn(null);
        Assert.assertEquals(null, userServiceImpl.getAuthByUsername("admin"));
    }

    /**
     * 测试正常逻辑
     */
    @Test
    public void testGetAuthByUsername_2() {
        UserAndRoleInfo userAndRoleInfo = new UserAndRoleInfo();
        userAndRoleInfo.setAuths("1,2,3,4,5,6");
        Mockito.when(tbUserMapper.selectRoleInfoByUsername(any())).thenReturn(userAndRoleInfo);
        Assert.assertEquals(6, userServiceImpl.getAuthByUsername("admin").getAuthList().size());
    }

    /**
     * 测试正常逻辑
     */
    @Test
    public void testUpdateByUsernameService_1() {
        Mockito.when(tbUserMapper.selectRoleInfoByUsername(any())).thenReturn(null);
        userServiceImpl.updateByUsernameService("admin", "admin");
        verify(tbUserMapper, times(1)).updateByParamSelective(any(), any());
    }


}
