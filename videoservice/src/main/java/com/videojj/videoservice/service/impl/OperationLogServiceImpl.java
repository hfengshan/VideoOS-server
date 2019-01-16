package com.videojj.videoservice.service.impl;

import com.videojj.videocommon.constant.Constants;
import com.videojj.videocommon.util.DateUtil;
import com.videojj.videoservice.annotation.OperationLogAnnotationService;
import com.videojj.videoservice.dao.TbOperationLogMapper;
import com.videojj.videoservice.dto.OperationLogPageInfoResponseDTO;
import com.videojj.videoservice.dto.PageInfoDTO;
import com.videojj.videoservice.entity.TbOperationLog;
import com.videojj.videoservice.entity.TbOperationLogExt;
import com.videojj.videoservice.enums.OperationLogTypeEnum;
import com.videojj.videoservice.service.OperationLogService;
import com.videojj.videoservice.util.PermissionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author @videopls.com
 * Created by  on 2018/10/10 下午2:59.
 * @Description:
 */
@Service
public class OperationLogServiceImpl implements OperationLogService {

    @Resource
    private TbOperationLogMapper tbOperationLogMapper;

    private static Logger log = LoggerFactory.getLogger(OperationLogServiceImpl.class);

    @Override
    public void addOperationLog(String operationDesc, Integer operationType) {
        String username = PermissionUtil.getCurrentUsername();
        tbOperationLogMapper.insertSelective(TbOperationLog.builder().operationDesc(operationDesc).
                operationType(operationType).creator(username).modifier(username).build());
        log.info("OperationLogServiceImpl.addOperationLog ==> 已经记录了用户[{}]的操作日志：{}",username,operationDesc);
    }


    @Override
    public OperationLogPageInfoResponseDTO queryAllByPage(Integer currentPage, Integer pageSize) {

        int limitstart=(currentPage-1) * pageSize;

        PageInfoDTO pageInfo=new PageInfoDTO(limitstart, pageSize);

        List<TbOperationLogExt> tbOperationLogList =  tbOperationLogMapper.selectByParamWithPage(pageInfo);

        OperationLogPageInfoResponseDTO operationLogPageInfoResponseDTO = new OperationLogPageInfoResponseDTO();

        operationLogPageInfoResponseDTO.setResCode(Constants.SUCESSCODE);

        operationLogPageInfoResponseDTO.setResMsg(Constants.COMMONSUCCESSMSG);

        Integer allcount = tbOperationLogMapper.count();

        if (allcount == 0){

            operationLogPageInfoResponseDTO.setTotalPage(0);

            operationLogPageInfoResponseDTO.setTotalRecord(0);

            return operationLogPageInfoResponseDTO;
        }

        List<OperationLogPageInfoResponseDTO.InnerOperationLog> interactionInfoList = null;

        if(!CollectionUtils.isEmpty(tbOperationLogList)) {

            interactionInfoList = batchTransform(tbOperationLogList);
        }
        operationLogPageInfoResponseDTO.setOperationLogList(interactionInfoList);

        if(allcount % pageSize==0){
            operationLogPageInfoResponseDTO.setTotalPage(allcount / pageSize);
        }else{
            operationLogPageInfoResponseDTO.setTotalPage((allcount / pageSize) + 1);
        }
        operationLogPageInfoResponseDTO.setTotalRecord(allcount);

        return operationLogPageInfoResponseDTO;
    }



    private List<OperationLogPageInfoResponseDTO.InnerOperationLog> batchTransform(List<TbOperationLogExt> tbOperationLogList) {

        List<OperationLogPageInfoResponseDTO.InnerOperationLog> innerOperationLogList = new ArrayList<>();

        for(TbOperationLogExt tbOperationLog:tbOperationLogList){

            OperationLogPageInfoResponseDTO.InnerOperationLog innerOperationLog = new OperationLogPageInfoResponseDTO.InnerOperationLog();

            innerOperationLog.setId(tbOperationLog.getId());

            innerOperationLog.setCreatedDate(DateUtil.toShortDateString(tbOperationLog.getGmtCreated()));

            innerOperationLog.setUsername(tbOperationLog.getUsername());

            innerOperationLog.setOperationDesc(tbOperationLog.getOperationDesc());

            innerOperationLogList.add(innerOperationLog);
        }

        return innerOperationLogList;
    }

    @OperationLogAnnotationService(descArgPositions = {0},type = OperationLogTypeEnum._23)
    @Override
    public void writeOperationLog_23(String templateName){}

    @OperationLogAnnotationService(descArgPositions = {0},type = OperationLogTypeEnum._42)
    @Override
    public void writeOperationLog_42(String launchPlanName){}

    @OperationLogAnnotationService(descArgPositions = {0},type = OperationLogTypeEnum._43)
    @Override
    public void writeOperationLog_43(String launchPlanName) {

    }

    @OperationLogAnnotationService(descArgPositions = {0},type = OperationLogTypeEnum._63)
    @Override
    public void writeOperationLog_63(String creativeName) {

    }

    @OperationLogAnnotationService(descArgPositions = {0},type = OperationLogTypeEnum._83)
    @Override
    public void writeOperationLog_83(String roleName) {

    }

    @OperationLogAnnotationService(descArgPositions = {0,1,2},type = OperationLogTypeEnum._102)
    @Override
    public void writeOperationLog_102(String username, String originalRoleName, String latestRoleName) {

    }

    @OperationLogAnnotationService(descArgPositions = {0},type = OperationLogTypeEnum._104)
    @Override
    public void writeOperationLog_104(String username) {

    }

}
