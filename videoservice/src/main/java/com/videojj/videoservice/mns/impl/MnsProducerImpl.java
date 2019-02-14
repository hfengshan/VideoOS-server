package com.videojj.videoservice.mns.impl;


import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudTopic;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.common.ClientException;
import com.aliyun.mns.common.ServiceException;
import com.aliyun.mns.model.Base64TopicMessage;
import com.aliyun.mns.model.TopicMessage;
import com.videojj.videoservice.config.MnsProperties;
import com.videojj.videoservice.mns.MnsProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MnsProducerImpl implements MnsProducer {

    private final static Logger logger = LoggerFactory.getLogger(MnsProducerImpl.class);

    private MNSClient mnsClient;

    @Autowired
    private MnsProperties mnsProperties;


    //String topicName,
    public void SendMessage(String message) {

        try {

            Map<String, Object> map = new HashMap<>();
            map.put("topicName", mnsProperties.getTopicName());
            map.put("mnsAccessKeyId", mnsProperties.getMnsAccessKeyId());
            map.put("mnsAccessKeySecret", mnsProperties.getMnsAccessKeySecret());
            map.put("mnsAccountEndPoint", mnsProperties.getMnsAccountEndPoint());

            CloudAccount account = new CloudAccount(mnsProperties.getMnsAccessKeyId(), mnsProperties.getMnsAccessKeySecret(), mnsProperties.getMnsAccountEndPoint());
            MNSClient client = account.getMNSClient(); //CloudAccount以及MNSClient单例实现即可，多线程安全
            CloudTopic topic = client.getTopicRef(mnsProperties.getTopicName());

            try {
                TopicMessage msg = new Base64TopicMessage();

                msg.setMessageBody(message);
                //msg.setMessageTag("filterTag"); //设置该条发布消息的filterTag
                msg = topic.publishMessage(msg);

                System.out.println(msg.getMessageId());
                System.out.println(msg.getMessageBodyMD5());
            } catch (Exception e) {

                e.printStackTrace();
                System.out.println("subscribe error"+e.toString());
            }

        } catch (ClientException ce) {

            ce.printStackTrace();
            logger.error("ClientException异常信息：" + ce.toString());

        } catch (ServiceException se) {
            se.printStackTrace();
            logger.error("ServiceException异常信息：" + se.toString());
        }
    }
}
