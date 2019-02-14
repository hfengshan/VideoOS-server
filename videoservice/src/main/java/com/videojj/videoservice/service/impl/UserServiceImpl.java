package com.videojj.videoservice.service.impl;

import com.videojj.videocommon.constant.Constants;
import com.videojj.videocommon.dto.base.BaseResponseDTO;
import com.videojj.videocommon.exception.ServiceException;
import com.videojj.videocommon.util.DateUtil;
import com.videojj.videocommon.util.MD5Util;
import com.videojj.videoservice.annotation.OperationLogAnnotationService;
import com.videojj.videoservice.bo.LoginBo;
import com.videojj.videoservice.bo.QueryUserParamBo;
import com.videojj.videoservice.dao.RedisSessionDao;
import com.videojj.videoservice.dao.TbRoleMapper;
import com.videojj.videoservice.dao.TbUserMapper;
import com.videojj.videoservice.dto.*;
import com.videojj.videoservice.entity.TbUser;
import com.videojj.videoservice.entity.TbUserCriteria;
import com.videojj.videoservice.entity.TbUserExt;
import com.videojj.videoservice.entity.UserAndRoleInfo;
import com.videojj.videoservice.enums.IsDeletedEnum;
import com.videojj.videoservice.enums.OperationLogTypeEnum;
import com.videojj.videoservice.service.OperationLogService;
import com.videojj.videoservice.service.UserService;
import com.videojj.videoservice.util.BaseSuccessResultUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/10 下午2:00.
 * @Description:
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private TbUserMapper tbUserMapper;

    @Resource
    private TbRoleMapper tbRoleMapper;

    @Resource
    private OperationLogService operationLogService;

    @Resource
    private RedisSessionDao redisSessionDao;

    @Resource
    private TransactionTemplate transactionTemplate;

    private static Logger log = LoggerFactory.getLogger("UserServiceImpl");

    @Override
    public BaseResponseDTO addUserService(AddUserRequestDTO request, String username) {

        TbUserCriteria qryParam = new TbUserCriteria();

        TbUserCriteria.Criteria userCri = qryParam.createCriteria();

//        userCri.andIsDeletedEqualTo(IsDeletedEnum.NO.getValue());

        userCri.andUsernameEqualTo(request.getUsername());

        List<TbUser> userList = tbUserMapper.selectByParam(qryParam);

        if(CollectionUtils.isNotEmpty(userList)){

            BaseResponseDTO res = new BaseResponseDTO();

            res.setResCode(Constants.FAILCODE);

            res.setResMsg("用户名已存在");

            return res;
        }

        TbUser param = new TbUser();
        param.setUsername(request.getUsername());
        param.setCreator(username);
        param.setModifier(username);
        param.setIsDeleted(IsDeletedEnum.NO.getValue());

        String encodePassword = MD5Util.EncoderByMd5(request.getPassword()).toLowerCase();

        param.setPassword(encodePassword);
        param.setRoleId(request.getRoleId());
        param.setGmtCreated(new Date());
        param.setGmtModified(new Date());
        try {

            tbUserMapper.insertSelective(param);

        }catch (Exception e){

            throw new ServiceException("插入用户信息时报错");
        }

        return BaseSuccessResultUtil.producessSuccess();
    }

    @Override
    public BaseResponseDTO updateUserService(UpdateUserRequestDTO request, String username) {
        /**如果传递过来的用户名和token里面存的用户名不一样，说明更改了用户名，那就查询更改后的用户名是否存在*/

        TbUser tbUser = tbUserMapper.selectByPrimaryKey(request.getUserId());
        /** 如果不等，即时更改了用户名*/
        if(!tbUser.getUsername().equals(request.getUsername())) {

            TbUserCriteria qryParam = new TbUserCriteria();
            TbUserCriteria.Criteria userCri = qryParam.createCriteria();
//            userCri.andIsDeletedEqualTo(IsDeletedEnum.NO.getValue());
            userCri.andUsernameEqualTo(request.getUsername());
            /**查询更改后的用户名，如果存在，即时被删除，也是不允许的*/
            List<TbUser> userList = tbUserMapper.selectByParam(qryParam);
            if (CollectionUtils.isNotEmpty(userList)) {
                BaseResponseDTO res = new BaseResponseDTO();

                res.setResMsg("用户名已存在");
                res.setResCode(Constants.FAILCODE);
                return res;
            }
        }

        TbUser param = new TbUser();
        param.setRoleId(request.getRoleId());
        if(null != request.getPassword()) {

            String encodePassword = MD5Util.EncoderByMd5(request.getPassword()).toLowerCase();

            param.setPassword(encodePassword);
        }
        param.setUsername(request.getUsername());
        param.setModifier(username);
        param.setId(request.getUserId());
        transactionTemplate.execute(new TransactionCallback<Boolean>() {

            @Override
            public Boolean doInTransaction(TransactionStatus transactionStatus) {

                try {
                    tbUserMapper.updateByPrimaryKeySelective(param);

                    redisSessionDao.removeSession(request.getUsername());
                }catch (Exception e){

                    log.error("UserServiceImpl.updateUserService ==> 更新用户信息报错，用户名为 {}",request.getUsername(),e);

                    throw new ServiceException("更新用户信息时报错");
                }

                return Boolean.TRUE;
            }
        });
        return BaseSuccessResultUtil.producessSuccess();
    }

    @Override
    public UserPageInfoResponseDTO queryPageInfoByParam(QueryUserRequestDTO param) {

        int limitstart=(param.getCurrentPage()-1) * param.getPageSize();
        QueryUserParamBo paramBo=new QueryUserParamBo(limitstart, param.getPageSize());
        if(null != param.getQryusername()){

            paramBo.setUsername(param.getQryusername());
        }

        List<TbUserExt> tbUserList =  tbUserMapper.selectByParamWithPage(paramBo);
        TbUserCriteria countParam = new TbUserCriteria();
        TbUserCriteria.Criteria userCriteria = countParam.createCriteria();
        userCriteria.andIsDeletedEqualTo(IsDeletedEnum.NO.getValue());
        int allcount = tbUserMapper.countByParam(countParam);
        UserPageInfoResponseDTO userPageInfoResponseDTO = new UserPageInfoResponseDTO();
        userPageInfoResponseDTO.setResCode(Constants.SUCESSCODE);
        userPageInfoResponseDTO.setResMsg(Constants.COMMONSUCCESSMSG);

        if (allcount == 0){

            userPageInfoResponseDTO.setTotalPage(0);

            userPageInfoResponseDTO.setTotalRecord(0);

            return userPageInfoResponseDTO;
        }
        if(CollectionUtils.isNotEmpty(tbUserList)) {
            // 此处要进行创建一个方法
            List<UserPageInfoResponseDTO.UserInfo> userInfoList = batchTransform(tbUserList);

            userPageInfoResponseDTO.setUserInfoList(userInfoList);
        }
        if(allcount % param.getPageSize()==0){
            userPageInfoResponseDTO.setTotalPage(allcount / param.getPageSize());
        }else{
            userPageInfoResponseDTO.setTotalPage((allcount / param.getPageSize()) + 1);
        }
        userPageInfoResponseDTO.setTotalRecord(allcount);

        return userPageInfoResponseDTO;
    }

    private List<UserPageInfoResponseDTO.UserInfo> batchTransform(List<TbUserExt> tbUserList) {

        List<UserPageInfoResponseDTO.UserInfo> userInfoList = new ArrayList<>();

        for(TbUserExt tbUserExt:tbUserList) {

            UserPageInfoResponseDTO.UserInfo userInfo = new UserPageInfoResponseDTO.UserInfo();

            userInfo.setRoleId(tbUserExt.getRoleId());
            userInfo.setCreateDate(DateUtil.toShortDateString(tbUserExt.getGmtCreated()));
            userInfo.setRoleName(tbUserExt.getRoleName());
            userInfo.setUserId(tbUserExt.getId());
            userInfo.setUserName(tbUserExt.getUsername());

            if(userInfo.getUserId().intValue() == 1){

                userInfo.setIsSuperRole(true);
            }else {

                userInfo.setIsSuperRole(false);
            }
            userInfoList.add(userInfo);
        }
        return userInfoList;
    }

    @Override
    public void deleteUserService(DeleteUserRequestDTO request, String username) {

        TbUser param = new TbUser();

        param.setId(request.getUserId());
        param.setIsDeleted(IsDeletedEnum.YES.getValue());
        param.setModifier(username);
        transactionTemplate.execute(new TransactionCallback<Boolean>() {

            @Override
            public Boolean doInTransaction(TransactionStatus transactionStatus) {

                try {
                    String userName = tbUserMapper.selectByPrimaryKey(request.getUserId()).getUsername();

                    tbUserMapper.updateByPrimaryKeySelective(param);
                    // 记录操作日志
                    operationLogService.writeOperationLog_104(userName);
                    redisSessionDao.removeSession(userName);

                    }catch (Exception e){

                    log.error("UserServiceImpl.deleteUserService ==> 逻辑删除用户时报错,用户名是 {}",username,e);

                    throw new ServiceException("逻辑删除用户信息时报错");
                }

                return Boolean.TRUE;
            }
        });

    }

    @Override
    public LoginBo getAuthByUsername(String username) {

        UserAndRoleInfo userAndRoleInfo= tbUserMapper.selectRoleInfoByUsername(username);

        if(null == userAndRoleInfo){

            return null;
        }
        LoginBo loginBo = new LoginBo();

        loginBo.setRoleId(userAndRoleInfo.getRoleId());
        loginBo.setRoleName(userAndRoleInfo.getRoleName());

        if(StringUtils.isNotEmpty(userAndRoleInfo.getAuths())) {

            String authsStr = userAndRoleInfo.getAuths();
            List<Integer> authList = new ArrayList<>();
            Arrays.asList(authsStr.replace(" ", "").split(",")).stream().forEach(x -> authList.add(Integer.parseInt(x.trim())));
            loginBo.setAuthList(authList);
        }
        return loginBo;
    }

    @OperationLogAnnotationService(descArgPositions = {0},type = OperationLogTypeEnum._103)
    @Override
    public void updateByUsernameService(String username, String password) {

        TbUser record = new TbUser();

        String encodePassword = MD5Util.EncoderByMd5(password).toLowerCase();

        record.setPassword(encodePassword);
        TbUserCriteria param = new TbUserCriteria();
        TbUserCriteria.Criteria userCri = param.createCriteria();

        userCri.andIsDeletedEqualTo("N");

        userCri.andUsernameEqualTo(username);

        try {

            tbUserMapper.updateByParamSelective(record,param);
        }catch (Exception e){

            throw new ServiceException("更新用户密码时报错");
        }

    }


}
