package com.videojj.videoservice.handler;

import com.videojj.videoservice.dao.TbLaunchPlanMapper;
import com.videojj.videoservice.dao.TbLaunchPlanOperationMapper;
import com.videojj.videoservice.dto.DeleteLaunchPlanRequestDTO;
import com.videojj.videoservice.entity.TbLaunchPlan;
import com.videojj.videoservice.entity.TbLaunchPlanOperation;
import com.videojj.videoservice.service.LaunchPlanService;
import com.videojj.videoservice.util.PermissionUtil;
import org.apache.commons.collections.CollectionUtils;
import org.crazycake.shiro.RedisManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;

import static com.videojj.videocommon.constant.Constants.PRELOAD_VIDEO_ID_REDIS_KEY_PREFIX;

/**
 * @Author zhangzhewen@videopls.com
 * Created by  on 2018/12/12 下午5:47.
 * @Description:
 */
@Service
public class LaunchPlanCloseService {

    @Resource
    private TbLaunchPlanOperationMapper tbLaunchPlanOperationMapper;

    @Resource
    private LaunchPlanService launchPlanService;

    private static final String USERNAME = "投放计划自动下线定时器";


    @Transactional(rollbackFor = Exception.class)
    public void run() {
        PermissionUtil.setCurrentUsername("即时投放自动下线");
        List<TbLaunchPlanOperation> finishedLaunchPlanOperationList = tbLaunchPlanOperationMapper.selectFinishedLaunchPlanOperation();
        if(CollectionUtils.isNotEmpty(finishedLaunchPlanOperationList)){
            for(TbLaunchPlanOperation tbLaunchPlan : finishedLaunchPlanOperationList){
                launchPlanService.offlineLaunchPlanService(new DeleteLaunchPlanRequestDTO(tbLaunchPlan.getId(),USERNAME));
            }
        }

    }

}
