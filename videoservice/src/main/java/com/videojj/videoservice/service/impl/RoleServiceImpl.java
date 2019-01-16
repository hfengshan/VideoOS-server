package com.videojj.videoservice.service.impl;

import com.videojj.videocommon.constant.Constants;
import com.videojj.videocommon.dto.base.BaseResponseDTO;
import com.videojj.videocommon.exception.ServiceException;
import com.videojj.videocommon.util.DateUtil;
import com.videojj.videoservice.dao.RedisSessionDao;
import com.videojj.videoservice.dao.TbNodeMapper;
import com.videojj.videoservice.dao.TbRoleMapper;
import com.videojj.videoservice.dao.TbUserMapper;
import com.videojj.videoservice.dto.*;
import com.videojj.videoservice.entity.*;
import com.videojj.videoservice.enums.IsDeletedEnum;
import com.videojj.videoservice.service.RoleService;
import com.videojj.videoservice.util.BaseSuccessResultUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/14 下午2:26.
 * @Description:
 */
@Service
public class RoleServiceImpl implements RoleService{

    @Resource
    private TbRoleMapper tbRoleMapper;

    @Resource
    private TbNodeMapper tbNodeMapper;

    @Resource
    private TbUserMapper tbUserMapper;

    @Resource
    private RedisSessionDao redisSessionDao;

    private static Logger log = LoggerFactory.getLogger("RoleServiceImpl");

    @Resource
    private TransactionTemplate transanctionTemplate;


    @Override
    public BaseResponseDTO addRoleService(AddRoleRequestDTO request, String username) {

        TbRoleCriteria qryParam = new TbRoleCriteria();

        TbRoleCriteria.Criteria roleCri = qryParam.createCriteria();

        roleCri.andRoleNameEqualTo(request.getRoleName());

        List<TbRole> roleList = tbRoleMapper.selectByParam(qryParam);

        if(!CollectionUtils.isEmpty(roleList)){

            BaseResponseDTO res = new BaseResponseDTO();

            res.setResCode(Constants.FAILCODE);

            res.setResMsg("角色名已存在");

            return res;
        }

        TbRole param = new TbRole();

        param.setRoleName(request.getRoleName());

        if(!CollectionUtils.isEmpty(request.getNodeIdList())) {

            param.setAuths(request.getNodeIdList().stream().map(s -> String.valueOf(s)).collect(Collectors.joining(",")));
        }
        param.setExtraInfo(getExtraInfo(request.getNodeIdList()));

        param.setCreator(username);

        param.setIsDeleted(IsDeletedEnum.NO.getValue());

        param.setModifier(username);

        try {

            tbRoleMapper.insertSelective(param);
        }catch (Exception e){

            log.error("RoleServiceImpl.addRoleService ==> add role error!!",e);

            throw new ServiceException("插入数据时报错!!");
        }

        return BaseSuccessResultUtil.producessSuccess();
    }

    @Override
    public BaseResponseDTO updateRoleService(UpdateRoleRequestDTO request, String username) {

        TbRoleCriteria qryParam = new TbRoleCriteria();

        TbRoleCriteria.Criteria roleCri = qryParam.createCriteria();

        roleCri.andRoleNameEqualTo(request.getRoleName());

        List<TbRole> roleList = tbRoleMapper.selectByParam(qryParam);

        if(!CollectionUtils.isEmpty(roleList)&&roleList.get(0).getId() != request.getRoleId().intValue()){

            BaseResponseDTO res = new BaseResponseDTO();

            res.setResCode(Constants.FAILCODE);

            res.setResMsg("角色名已存在");

            return res;
        }

        TbRole param = new TbRole();

        param.setModifier(username);

        param.setAuths(request.getNodeIdList().stream().map(s -> s.toString()).collect(Collectors.joining(",")));

        String extraInfo = getExtraInfo(request.getNodeIdList());

        param.setExtraInfo(extraInfo);

        param.setId(request.getRoleId());

        param.setRoleName(request.getRoleName());

        transanctionTemplate.execute(new TransactionCallback<Boolean>() {

            @Override
            public Boolean doInTransaction(TransactionStatus transactionStatus) {
                try {
                    tbRoleMapper.updateByPrimaryKeySelective(param);

                    deleteSession(request.getRoleId());
                }catch (Exception e){

                    log.error("RoleServiceImpl.updateRoleService ==> update role error!!",e);

                    throw new ServiceException("更新角色信息报错!!");
                }
                return Boolean.TRUE;
            }

        });
        return BaseSuccessResultUtil.producessSuccess();
    }

    private String getExtraInfo(List<Integer> nodeIdList) {

        if(CollectionUtils.isEmpty(nodeIdList)){

            return null;
        }

        TbNodeCriteria nodeParam = new TbNodeCriteria();

        TbNodeCriteria.Criteria nodeCri = nodeParam.createCriteria();

        nodeCri.andIdIn(nodeIdList);

        nodeCri.andIsDeletedEqualTo(IsDeletedEnum.NO.getValue());

        try {

            List<TbNode> tbNodeList = tbNodeMapper.selectByParam(nodeParam);

            return tbNodeList.stream().map(TbNode::getDesc).collect(Collectors.joining(","));
        }catch (Exception e){

            log.error("RoleServiceImpl.getExtraInfo ==> query by param error!!",e);

            throw new ServiceException("查询数据时报错!!");
        }
    }

    @Override
    public void deleteRoleService(DeleteRoleRequestDTO request, String username) {

        TbRole param = new TbRole();

        param.setId(request.getRoleId());

        param.setModifier(username);

        param.setIsDeleted(IsDeletedEnum.YES.getValue());

        transanctionTemplate.execute(new TransactionCallback<Boolean>() {

            @Override
            public Boolean doInTransaction(TransactionStatus transactionStatus) {

                try {

                    tbRoleMapper.updateByPrimaryKeySelective(param);

                    deleteSession(request.getRoleId());

                }catch (Exception e){

                    log.error("RoleServiceImpl.deleteRoleService ==> delete role error!!",e);

                    throw new ServiceException("逻辑删除数据时报错!");
                }

                return Boolean.TRUE;
            }
        });


    }

    private void deleteSession(Integer roleId) {

        TbUserCriteria userParam = new TbUserCriteria();

        TbUserCriteria.Criteria cri = userParam.createCriteria();

        cri.andRoleIdEqualTo(roleId).andIsDeletedEqualTo("N");

        List<TbUser> tbUserList = tbUserMapper.selectByParam(userParam);

        if(!CollectionUtils.isEmpty(tbUserList)){

            tbUserList.stream().forEach((k) -> redisSessionDao.removeSession(k.getUsername()));

        }
    }

    @Override
    public RolePageInfoResponseDTO queryPageInfoByParam(QueryRoleRequestDTO param) {

        RolePageInfoResponseDTO rolePageInfoResponseDTO = new RolePageInfoResponseDTO();

        TbRoleCriteria qryparam = new TbRoleCriteria();

        qryparam.setOrderByClause("gmt_created desc");

        TbRoleCriteria.Criteria tbRoleCriteria = qryparam.createCriteria();

        tbRoleCriteria.andIsDeletedEqualTo(IsDeletedEnum.NO.getValue());

        int limitstart=(param.getCurrentPage()-1) * param.getPageSize();

        PageInfoDTO paramBo=new PageInfoDTO(limitstart, param.getPageSize());

        List<TbRole> tbRoleList =  tbRoleMapper.selectByParamWithPage(qryparam,paramBo);

        int allcount = tbRoleMapper.countByParam(qryparam);

        rolePageInfoResponseDTO.setResCode(Constants.SUCESSCODE);

        rolePageInfoResponseDTO.setResMsg(Constants.COMMONSUCCESSMSG);

        if(allcount ==0 ){
            rolePageInfoResponseDTO.setTotalPage(0);

            rolePageInfoResponseDTO.setTotalRecord(0);

            return rolePageInfoResponseDTO;

        }

        if(allcount % param.getPageSize()==0){
            rolePageInfoResponseDTO.setTotalPage(allcount / param.getPageSize());
        }else{
            rolePageInfoResponseDTO.setTotalPage((allcount / param.getPageSize()) + 1);
        }
        rolePageInfoResponseDTO.setTotalRecord(allcount);

        List<RolePageInfoResponseDTO.RoleInfo> roleInfoList = convertParam(tbRoleList);

        rolePageInfoResponseDTO.setRoleInfoList(roleInfoList);

        return rolePageInfoResponseDTO;
    }

    @Override
    public RoleOwnAuthInfoResponseDTO queryAuthInfoByParam(Integer roleId) {

        TbRole role = tbRoleMapper.selectByPrimaryKey(roleId);

        RoleOwnAuthInfoResponseDTO roleOwnAuthInfoResponseDTO = new RoleOwnAuthInfoResponseDTO();

        roleOwnAuthInfoResponseDTO.setRoleName(role.getRoleName());

        if(StringUtils.isNotEmpty(role.getAuths())) {

            roleOwnAuthInfoResponseDTO.setNodeIdList(Arrays.asList(role.getAuths().split(",")).stream().map(x -> Integer.parseInt(x)).collect(Collectors.toList()));
        }

        roleOwnAuthInfoResponseDTO.setRoleId(roleId);

        roleOwnAuthInfoResponseDTO.setResCode(Constants.SUCESSCODE);

        roleOwnAuthInfoResponseDTO.setResMsg(Constants.COMMONSUCCESSMSG);

        return roleOwnAuthInfoResponseDTO;
    }

    @Override
    public AllRoleInfoResponseDTO queryAll() {

        TbRoleCriteria param = new TbRoleCriteria();

        param.setOrderByClause("gmt_created desc");

        TbRoleCriteria.Criteria pcri = param.createCriteria();

        pcri.andIsDeletedEqualTo(IsDeletedEnum.NO.getValue());

        List<TbRole> taRoleList = tbRoleMapper.selectByParam(param);

        AllRoleInfoResponseDTO res = new AllRoleInfoResponseDTO();

        res.setResMsg(Constants.COMMONSUCCESSMSG);

        res.setResCode(Constants.SUCESSCODE);

        res.setRoleInfoList(convertRes(taRoleList));

        return res;
    }

    private List<AllRoleInfoResponseDTO.RoleInfo> convertRes(List<TbRole> tbRoleList) {

        if(CollectionUtils.isEmpty(tbRoleList)){
            return null;
        }

        List<AllRoleInfoResponseDTO.RoleInfo> res = new ArrayList<>();

        for(TbRole tbRole:tbRoleList){
            AllRoleInfoResponseDTO.RoleInfo roleInfo = new AllRoleInfoResponseDTO.RoleInfo();

            roleInfo.setRoleId(tbRole.getId());

            roleInfo.setRoleName(tbRole.getRoleName());

            if(tbRole.getId() == 1){

                roleInfo.setIsSuperRole(true);
            }

            res.add(roleInfo);
        }
        return res;
    }

    private List<RolePageInfoResponseDTO.RoleInfo> convertParam(List<TbRole> tbRoleList) {

        List<RolePageInfoResponseDTO.RoleInfo> roleInfoList = new ArrayList<>();

        for(TbRole tbRole:tbRoleList){

            RolePageInfoResponseDTO.RoleInfo roleInfo = new RolePageInfoResponseDTO.RoleInfo();

            roleInfo.setRoleName(tbRole.getRoleName());

            roleInfo.setCreateDate(DateUtil.toShortDateString(tbRole.getGmtCreated()));

            roleInfo.setRoleDesc(tbRole.getExtraInfo());

            roleInfo.setRoleId(tbRole.getId());

            if(tbRole.getId() == 1){

                roleInfo.setIsSuperRole(true);
            }

            roleInfoList.add(roleInfo);

        }

        return roleInfoList;

    }


}
