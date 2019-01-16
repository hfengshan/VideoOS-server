package com.videojj.videoservice.emqttd;

import com.videojj.videocommon.exception.ServiceException;
import com.videojj.videoservice.cache.LaunchPlanCache;
import com.videojj.videoservice.config.MqttProperties;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @auther
 * @date 2018/5/28 9:20
 */
@Component
public class PushCallback implements MqttCallback {

    private static Logger log = LoggerFactory.getLogger("MqttPushClient");

    @Resource
    private LaunchPlanCache launchPlanCache;

    @Resource
    private MqttProperties mqttProperties;

    @Resource
    private MqttPushClient mqttPushClient;

    public void connectionLost(Throwable cause) {

        log.info("PushCallback.connectionLost ==> 连接已经断开，可以重新连接");

    }


    public void deliveryComplete(IMqttDeliveryToken token) {



        log.info("PushCallback.deliveryComplete ==> 发送成功，result {},",token.isComplete());


    }

    public void messageArrived(String topic, MqttMessage message) throws Exception {

        // 此处是接受到消息的回调  message.getQos()  message.getPayload()消息体

        log.info("收到的消息是：{}",new String(message.getPayload()));

    }

    public MqttPushClient getMqttPushClient() {
        return mqttPushClient;
    }

    public void setMqttPushClient(MqttPushClient mqttPushClient) {
        this.mqttPushClient = mqttPushClient;
    }
}
