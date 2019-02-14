package com.videojj.videoservice.emqttd;

import com.alibaba.fastjson.JSONObject;
import com.videojj.videoservice.apidto.LaunchApiQueryInfoResponseDTO;
import com.videojj.videoservice.config.MqttProperties;
import com.videojj.videoservice.encry.CommonAesService;
import com.videojj.videoservice.encry.CommonRSAService;
import com.videojj.videoservice.util.EncryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/20 下午4:03.
 * @Description:
 */
@Service
public class MqttService {

    @Resource
    private MqttPushClient mqttPushClient;

    private static Logger log = LoggerFactory.getLogger("MqttService");

    @Resource
    protected CommonAesService commonAesService;

    @Resource
    private PushCallback pushCallback;

    @Resource
    private MqttProperties mqttConfig;

    public void push(String topic, LaunchApiQueryInfoResponseDTO.LaunchInfo launchInfo) {

        log.info("MqttService.push ==> topic is {}",topic);

        LaunchApiQueryInfoResponseDTO res = new LaunchApiQueryInfoResponseDTO();

        List<LaunchApiQueryInfoResponseDTO.LaunchInfo> launchInfos = new ArrayList<>();

        launchInfos.add(launchInfo);

        res.setLaunchInfoList(launchInfos);

        String launchInfoJson = commonAesService.encryResult(res);

        log.info("MqttService.push ==> launchInfoJson is {}",launchInfoJson);

        mqttPushClient.publish(topic , launchInfoJson,pushCallback);

    }

//    public void pushVideoId(String topic, String videoId) {
//
//        log.info("MqttService.push ==> topic is {}",topic);
//
//        mqttPushClient.publish(topic , videoId,pushCallback);
//
//    }
}
