package com.videojj.videoservice.service.impl;

import com.github.pagehelper.Page;
import com.google.common.io.ByteStreams;
import com.videojj.videocommon.constant.Constants;
import com.videojj.videocommon.exception.ServiceException;
import com.videojj.videocommon.util.DateUtil;
import com.videojj.videoservice.annotation.OperationLogAnnotationService;
import com.videojj.videoservice.config.CommonConfig;
import com.videojj.videoservice.dao.TbCreativeMapper;
import com.videojj.videoservice.dao.TbInteractionMapper;
import com.videojj.videoservice.dao.TbTemplateMapper;
import com.videojj.videoservice.dto.AllInteractionInfoResponseDTO;
import com.videojj.videoservice.dto.InteractionInfoResponseDTO;
import com.videojj.videoservice.dto.InteractionPageInfoResponseDTO;
import com.videojj.videoservice.dto.QueryInteractionInfoResponseDTO;
import com.videojj.videoservice.entity.TbInteraction;
import com.videojj.videoservice.enums.IsDeletedEnum;
import com.videojj.videoservice.enums.OperationLogTypeEnum;
import com.videojj.videoservice.service.InteractionTypeService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/1 下午2:59.
 * @Description:
 */
@Service
public class InteractionTypeServiceImpl implements InteractionTypeService {

    @Resource
    private TbInteractionMapper tbInteractionMapper;

    private static Logger log = LoggerFactory.getLogger(InteractionTypeServiceImpl.class);

    @Resource
    private TbTemplateMapper tbTemplateMapper;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private TbCreativeMapper tbCreativeMapper;

    @Resource
    private CommonConfig commonConfig;

    /**
     * @Author:
     * @Description:
     * @Date: 下午3:00 2018/8/1
     */
    @OperationLogAnnotationService(descArgPositions = {2},type = OperationLogTypeEnum._1)
    @Override
    public void addInteractionInfo(InputStream fileInputStream, String fileName, String interactionTypeName,String username) {

        TbInteraction condition = new TbInteraction();
        condition.setInteractionTypeName(interactionTypeName);
        List<TbInteraction> infoList = tbInteractionMapper.select(condition);

        if(!CollectionUtils.isEmpty(infoList)){

            throw new ServiceException("应用名称重复，请更换应用名称");
        }

        try {
            String content = new String(ByteStreams.toByteArray(fileInputStream));

            TbInteraction tbInteraction = new TbInteraction();

            tbInteraction.setInteractionTypeName(interactionTypeName);

            tbInteraction.setFileName(fileName);

            tbInteraction.setContent(content);

            tbInteraction.setCreator(username);

            tbInteraction.setModifier(username);

            tbInteractionMapper.insertSelective(tbInteraction);

        } catch (Exception e) {

            log.error("InteractionTypeServiceImpl.addInteractionInfo ==> saveFile error!!!",e);

            throw new ServiceException("saveFile Error!! please check the param...");
        }


    }
    /**
     * @Author:
     * @Description:
     * @Date: 下午5:39 2018/8/1
     */
    @Override
    public InteractionPageInfoResponseDTO queryAllByPage(Integer currentPage, Integer pageSize) {

        Page<TbInteraction> tbInteractionList =  tbInteractionMapper.selectPage();
        InteractionPageInfoResponseDTO interactionPageInfoResponseDTO = new InteractionPageInfoResponseDTO();
        interactionPageInfoResponseDTO.setResCode(Constants.SUCESSCODE);
        interactionPageInfoResponseDTO.setResMsg(Constants.COMMONSUCCESSMSG);

        if (tbInteractionList.getTotal() == 0){
            interactionPageInfoResponseDTO.setTotalPage(0);
            interactionPageInfoResponseDTO.setTotalRecord(0L);
            return interactionPageInfoResponseDTO;
        }

        interactionPageInfoResponseDTO.setInteractionInfoList(batchTransform(tbInteractionList));
        interactionPageInfoResponseDTO.setTotalPage(tbInteractionList.getPages());
        interactionPageInfoResponseDTO.setTotalRecord(tbInteractionList.getTotal());

        return interactionPageInfoResponseDTO;
    }


    @OperationLogAnnotationService(descArgPositions = {0},type = OperationLogTypeEnum._3)
    @Override
    public void deleteInteractionByTypeName(String interactionTypeName,String username){

        transactionTemplate.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean doInTransaction(TransactionStatus tranStatus) {
                try {

                    tbInteractionMapper.logicallyDeleteByInteractionTypeName(interactionTypeName,username);
                    /**如果逻辑删除类型，相应的模版和素材都逻辑删除
                     * 2018-11-30 by zhangzhewen*/
                    tbTemplateMapper.logicallyDeleteByInteractionTypeName(interactionTypeName,username);
                    tbCreativeMapper.logicallyDeleteByInteractionTypeName(interactionTypeName,username);

                }catch (Exception e){

                    log.error("InteractionTypeServiceImpl.deleteInteractionByTypeName ==> update database error!!interactionTypeName is {}",interactionTypeName,e);

                    throw new ServiceException("updateDataBase error..please check param");
                }
                return Boolean.TRUE;
            }
        });
    }

    @Override
    public InteractionInfoResponseDTO queryByParam(String interactionTypeName) {

        TbInteraction condition = new TbInteraction();
        condition.setIsDeleted(IsDeletedEnum.NO.getValue());
        if(StringUtils.isNotEmpty(interactionTypeName)){
            condition.setInteractionTypeName(interactionTypeName);
        }
        List<TbInteraction> tbInteractions = tbInteractionMapper.select(condition);

        InteractionInfoResponseDTO interactionInfoDTO = new InteractionInfoResponseDTO();

        interactionInfoDTO.setResMsg(Constants.SUCESSCODE);

        interactionInfoDTO.setResMsg(Constants.COMMONSUCCESSMSG);

        if(CollectionUtils.isEmpty(tbInteractions)){

            return interactionInfoDTO;
        }
        interactionInfoDTO.setInteractionTypeName(interactionTypeName);

        interactionInfoDTO.setInteractionTypeId(tbInteractions.get(0).getId());

        return interactionInfoDTO;
    }

    @OperationLogAnnotationService(descArgPositions = {2},type = OperationLogTypeEnum._2)
    @Override
    public void updateInteractionInfo(MultipartFile multipartFile, String fileName, String interactionTypeName, String username, Integer interactionTypeId){
        try {

            TbInteraction tbInteraction = new TbInteraction();

            if(StringUtils.isNotEmpty(interactionTypeName)) {
                tbInteraction.setInteractionTypeName(interactionTypeName);
            }

            if(StringUtils.isNotEmpty(fileName)) {
                tbInteraction.setFileName(fileName);
            }

            if(null != multipartFile) {

                InputStream in = multipartFile.getInputStream();

                String filecontent = new String(ByteStreams.toByteArray(in));

                tbInteraction.setContent(filecontent);
            }

            tbInteraction.setModifier(username);
            tbInteraction.setId(interactionTypeId);

            tbInteractionMapper.updateByPrimaryKeySelective(tbInteraction);

            /**
             * 应用名称变更，相应的模版，素材引用的这个类型的名称跟着变更
             * 2018-11-30 by zhangzhewen
             */
            tbTemplateMapper.updateInteractionTypeNameByInteractionId(interactionTypeId);
            tbCreativeMapper.updateInteractionTypeNameByInteractionId(interactionTypeId);

        } catch (Exception e) {

            log.error("InteractionTypeServiceImpl.updateInteractionInfo ==> update database error!!!interactionTypeName is {}",interactionTypeName,e);

            throw new ServiceException("update database error! please check param");
        }
    }

    @Override
    public AllInteractionInfoResponseDTO queryAll() {
        AllInteractionInfoResponseDTO allInter = new AllInteractionInfoResponseDTO();
        allInter.setInteractionInfoList(convertResult(tbInteractionMapper.selectPage()));
        allInter.setResCode(Constants.SUCESSCODE);
        allInter.setResMsg(Constants.COMMONSUCCESSMSG);
        return allInter;
    }

    @Override
    public InteractionInfoResponseDTO queryById(Integer interactionId) {

        TbInteraction tbInteraction = tbInteractionMapper.selectByPrimaryKey(interactionId);

        InteractionInfoResponseDTO res = new InteractionInfoResponseDTO();

        res.setResCode(Constants.SUCESSCODE);

        res.setResMsg(Constants.COMMONSUCCESSMSG);

        if(null == tbInteraction){

            return res;
        }

        res.setConfigInfo(tbInteraction.getContent());

        res.setInteractionTypeId(tbInteraction.getId());

        res.setInteractionTypeName(tbInteraction.getInteractionTypeName());

        res.setFileName(tbInteraction.getFileName());

        return res;
    }

    @Override
    public List<TbInteraction> selectByName(String interactionTypeName) {
        TbInteraction condition = new TbInteraction();
        condition.setInteractionTypeName(interactionTypeName);
        condition.setIsDeleted(IsDeletedEnum.NO.getValue());
        return tbInteractionMapper.select(condition);

    }

    @Override
    public String getInteractionDefaultImgUrl() {
        return commonConfig.getFileDomainName().concat(commonConfig.getPreKey()).concat(Constants.INTERACTION_DEFAULT_JPG);
    }

    @Override
    public QueryInteractionInfoResponseDTO queryInteractionInfo(Integer creativeId) {

        QueryInteractionInfoResponseDTO res = new QueryInteractionInfoResponseDTO();
        res.setResCode(Constants.SUCESSCODE);
        res.setResMsg(Constants.COMMONSUCCESSMSG);
        Map<String,Object> data = tbInteractionMapper.selectContentAndHotspot(creativeId);
        if(data == null){
            return res;
        }
        res.setInteractionInfo((String)data.get("content"));
        res.setHotspot((Integer)data.get("hotspot"));

        return res;
    }

    private List<AllInteractionInfoResponseDTO.InteractionInfo> convertResult(List<TbInteraction> interactionList) {

        if(CollectionUtils.isEmpty(interactionList)){
            return null;
        }
        List<AllInteractionInfoResponseDTO.InteractionInfo> res = new ArrayList<>();
        for(TbInteraction tbInter:interactionList){

            AllInteractionInfoResponseDTO.InteractionInfo interInfo = new AllInteractionInfoResponseDTO.InteractionInfo();

            interInfo.setInteractionId(tbInter.getId());

            interInfo.setInteractionTypeName(tbInter.getInteractionTypeName());

            if(StringUtils.isEmpty(tbInter.getImgUrl())){
                interInfo.setImgUrl(getInteractionDefaultImgUrl());
            }else{
                interInfo.setImgUrl(tbInter.getImgUrl());
            }

            interInfo.setIsSystem(tbInter.getIsSystem());

            res.add(interInfo);
        }

        return res;

    }

    private List<InteractionPageInfoResponseDTO.InnerInteractionInfo> batchTransform(List<TbInteraction> tbInteractionList) {

        List<InteractionPageInfoResponseDTO.InnerInteractionInfo> interactionInfoList = new ArrayList<>();

        for(TbInteraction tbInteraction:tbInteractionList){

            InteractionPageInfoResponseDTO.InnerInteractionInfo interactionInfo = new InteractionPageInfoResponseDTO.InnerInteractionInfo();

            interactionInfo.setCreateDate(DateUtil.toShortDateString(tbInteraction.getGmtCreated()));

            interactionInfo.setInteractionTypeId(tbInteraction.getId());

            interactionInfo.setInteractionTypeName(tbInteraction.getInteractionTypeName());

            interactionInfoList.add(interactionInfo);
        }

        return interactionInfoList;
    }


}
