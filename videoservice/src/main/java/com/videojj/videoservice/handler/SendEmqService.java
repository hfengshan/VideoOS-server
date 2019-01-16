package com.videojj.videoservice.handler;

import com.videojj.videocommon.util.DateUtil;
import com.videojj.videoservice.apidto.LaunchApiQueryInfoResponseDTO;
import com.videojj.videoservice.dao.TbLaunchPlanMapper;
import com.videojj.videoservice.dao.TbLaunchTaskExecuteMapper;
import com.videojj.videoservice.emqttd.MqttService;
import com.videojj.videoservice.entity.TbLaunchPlanApiInfoExt;
import com.videojj.videoservice.entity.TbLaunchTaskExecute;
import com.videojj.videoservice.enums.LaunchTimeTypeEnum;
import com.videojj.videoservice.service.impl.ExchangeDataUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/31 下午3:46.
 * @Description:
 */
@Service
public class SendEmqService {

    @Resource
    private TbLaunchTaskExecuteMapper tbLaunchTaskExecuteMapper;

    private static Logger log = LoggerFactory.getLogger("SendEmqService");

    @Resource
    private MqttService mqttService;

    @Resource
    private TbLaunchPlanMapper tbLaunchPlanMapper;

    public void exec(String jobName, Integer launchPlanId) {

        TbLaunchTaskExecute addParam = new TbLaunchTaskExecute();

        addParam.setLaunchPlanId(launchPlanId);

        addParam.setExecuteStatus((byte) 0);

        addParam.setExecuteName(jobName.concat(DateUtil.toShortDateString(new Date())));

        addParam.setExecuteTime(new Date());

        addParam.setCreator("system");

        addParam.setModifier("system");

        try {

            tbLaunchTaskExecuteMapper.insertSelective(addParam);

        }catch(DataIntegrityViolationException de) {

            if (de.getMessage().contains("Duplicate entry")) {

                log.info("SendEmqService.exec ==> this execute has exist, jobName is {}", de,jobName);

                return ;
            }
        }catch (Exception e){

            log.error("SendEmqService.exec ==> tbLaunchTaskExecuteMapper insert error!!jobName is {}",e,jobName);
        }

        List<TbLaunchPlanApiInfoExt> launchPlanExt = tbLaunchPlanMapper.selectByVideoId(launchPlanId,null,null, LaunchTimeTypeEnum.BJ_TIME.getValue());

        if(CollectionUtils.isEmpty(launchPlanExt)){

            return ;
        }
        /**即时是北京时间的，投放的时候，是当作实时投放来处理的*/
        List<LaunchApiQueryInfoResponseDTO.LaunchInfo> result = null;
        try {
            result = ExchangeDataUtil.convertRealTimeResult(launchPlanExt,jobName,false);
        } catch (Exception e) {

            log.error("SendEmqService.exec ==> exchange data error!!planId is {}",launchPlanId,e);

            return;

        }

        mqttService.push(launchPlanExt.get(0).getLaunchVideoId(),result.get(0));

        TbLaunchTaskExecute udpParam = new TbLaunchTaskExecute();

        udpParam.setId(addParam.getId());

        udpParam.setExecuteStatus((byte) 1);

        tbLaunchTaskExecuteMapper.updateByPrimaryKeySelective(udpParam);

    }

}
