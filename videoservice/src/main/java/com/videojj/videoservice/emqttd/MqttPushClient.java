package com.videojj.videoservice.emqttd;

import com.videojj.videocommon.exception.ServiceException;
import com.videojj.videoservice.apidto.LaunchApiQueryInfoResponseDTO;
import com.videojj.videoservice.config.MqttProperties;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
public class MqttPushClient {

    @Autowired
    private MqttPahoClientFactory mqttPahoClientFactory;

    private static Logger log = LoggerFactory.getLogger("MqttPushClient");

    private IMqttClient client =null;

    @Resource
    private MqttProperties mqttConfig;

    public synchronized IMqttClient connect(PushCallback pushCallback){
        try {

            if (client == null) {

                client = mqttPahoClientFactory.getClientInstance(mqttConfig.getHostUrl(), mqttConfig.getClientId());
            }

            client.setCallback(pushCallback);

            if(!client.isConnected()) {

                Thread.sleep(50l);

                client.connect();
            }

            return client;

        } catch (Exception e) {

            log.error("emqttd connect error!!",e);

            throw new ServiceException("连接emqttd服务器报错了");
        }
    }


    /**
     * 发布，默认qos为2，保证每个客户端只收到一次
     * @param topic
     * @param
     */
    public void publish(String topic,LaunchApiQueryInfoResponseDTO.LaunchInfo launchInfo){
        publish(2, false, topic, launchInfo,null);
    }

    /**消息题全部使用json格式*/
    public void publish(String topic,String launchInfo,PushCallback pushCallback){
        publish(2, false, topic, launchInfo,pushCallback);
    }

    /**
     * 发布  发布的是byte字节串
     * @param qos
     * @param retained
     * @param topic
     * @param pushMessage
     */
    public synchronized <T> void  publish(int qos,boolean retained,String topic,T pushMessage,PushCallback pushCallback) {

        IMqttClient client = connect(pushCallback);

        MqttMessage message = new MqttMessage();

        message.setQos(qos);

        message.setRetained(retained);

        message.setPayload(pushMessage.toString().getBytes());

//        if(null == mTopic){
//
//            log.error("topic not exist");
//        }
        MqttDeliveryToken token;
        try {

            MqttTopic mTopic = client.getTopic(topic);

            token = mTopic.publish(message);

            token.waitForCompletion();

        } catch (MqttPersistenceException e) {

            log.error("persistence error..", e);

            throw new ServiceException("持久化消息的时候报错");
        } catch (MqttException e) {

            log.error("publish error..", e);

            throw new ServiceException("发布主题的时候报错");

        }
    }

    /**
     * 订阅某个主题，qos默认为0
     * @param topic
     */
    public void subscribe(String topic){

        subscribe(topic,0);
    }

    /**
     * 订阅某个主题
     * @param topic
     * @param qos
     */
    public void subscribe(String topic,int qos){

        try {

            IMqttClient client = connect(null);

            client.subscribe(topic, qos);

        } catch (MqttException e) {

            log.error("subscribe error!!",e);

            throw new ServiceException("订阅主题报错了");
        }
    }

}