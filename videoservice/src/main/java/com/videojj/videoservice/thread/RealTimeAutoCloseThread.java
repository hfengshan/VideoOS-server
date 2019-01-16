package com.videojj.videoservice.thread;

import com.videojj.videoservice.dao.TbLaunchPlanMapper;
import com.videojj.videoservice.dao.TbLaunchPlanOperationMapper;
import com.videojj.videoservice.dto.DeleteLaunchPlanRequestDTO;
import com.videojj.videoservice.entity.TbLaunchPlan;
import com.videojj.videoservice.entity.TbLaunchPlanOperation;
import com.videojj.videoservice.enums.LaunchStatusEnum;
import com.videojj.videoservice.service.LaunchPlanService;
import com.videojj.videoservice.util.PermissionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @Author zhangzhewen@videopls.com
 * Created by  on 2018/12/18 下午12:18.
 * @Description:
 */
public class RealTimeAutoCloseThread implements Runnable {

    private LaunchPlanService launchPlanService;

    private long launchLenTime;

    private DeleteLaunchPlanRequestDTO deleteLaunchPlanRequestDTO;

    private TbLaunchPlanMapper tbLaunchPlanMapper;

    private TbLaunchPlanOperationMapper tbLaunchPlanOperationMapper;

    private static Logger log = LoggerFactory.getLogger(RealTimeAutoCloseThread.class);

    public RealTimeAutoCloseThread(LaunchPlanService launchPlanService, long launchLenTime, DeleteLaunchPlanRequestDTO deleteLaunchPlanRequestDTO,
                                   TbLaunchPlanMapper tbLaunchPlanMapper, TbLaunchPlanOperationMapper tbLaunchPlanOperationMapper) {

        this.launchPlanService = launchPlanService;

        this.launchLenTime = launchLenTime;

        this.deleteLaunchPlanRequestDTO = deleteLaunchPlanRequestDTO;

        this.tbLaunchPlanMapper = tbLaunchPlanMapper;

        this.tbLaunchPlanOperationMapper = tbLaunchPlanOperationMapper;
    }

    @Override
    public void run() {

        try {
            Thread.sleep(launchLenTime * 1000);
        } catch (InterruptedException e) {
            log.error("RealTimeAutoCloseThread--------->error! ", e);
            return;
        }

//        PermissionUtil.setCurrentUsername("即时投放自动下线");
//        launchPlanService.offlineLaunchPlanService(deleteLaunchPlanRequestDTO);
        /**重新更新问待投放*/
        TbLaunchPlanOperation launchPlanOperation = new TbLaunchPlanOperation();
        launchPlanOperation.setStatus(LaunchStatusEnum.WAIT_PASS.getValue());
        launchPlanOperation.setId(deleteLaunchPlanRequestDTO.getLaunchPlanId());
        tbLaunchPlanOperationMapper.updateByPrimaryKeySelective(launchPlanOperation);
        tbLaunchPlanMapper.updateStatusByOperationId(LaunchStatusEnum.WAIT_PASS.getValue(),deleteLaunchPlanRequestDTO.getLaunchPlanId());

    }

    public void start(){
        new Thread(this).start();
    }

}
