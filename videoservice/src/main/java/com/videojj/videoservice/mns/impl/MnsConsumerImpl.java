package com.videojj.videoservice.mns.impl;


import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudQueue;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.common.ClientException;
import com.aliyun.mns.common.ServiceException;
import com.aliyun.mns.model.Message;
import com.videojj.videoservice.cache.LaunchPlanCache;
import com.videojj.videoservice.config.MnsProperties;
import com.videojj.videoservice.mns.MnsConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class MnsConsumerImpl implements MnsConsumer {

    private final static Logger logger = LoggerFactory.getLogger(MnsConsumerImpl.class);

    //    @Autowired
    private MNSClient mnsClient;

    @Autowired
    private MnsProperties mnsProperties;

    @Resource
    private LaunchPlanCache launchPlanCache;


    public void getMessage(String queueName){

        Map<String, Object> map = new HashMap<>();
        map.put("topicName", mnsProperties.getTopicName());
        map.put("mnsAccessKeyId", mnsProperties.getMnsAccessKeyId());
        map.put("mnsAccessKeySecret", mnsProperties.getMnsAccessKeySecret());
        map.put("mnsAccountEndPoint", mnsProperties.getMnsAccountEndPoint());

        CloudAccount account = new CloudAccount(mnsProperties.getMnsAccessKeyId(), mnsProperties.getMnsAccessKeySecret(), mnsProperties.getMnsAccountEndPoint());
        MNSClient client = account.getMNSClient();
        CloudQueue queue = client.getQueueRef(queueName);

        try{

            Message popMsg  = queue.popMessage();

            //消费消息 删除缓存
            String videoId = new String(popMsg.getMessageBody());

            launchPlanCache.remove(videoId);

            if(popMsg!= null){

                System.out.println("message handle:"+popMsg.getMessageId());
            }

        }catch (ClientException ce){

            logger.error("ClientException信息："+ce.toString());

        }catch (ServiceException se){

            logger.error("ServiceException信息："+se.toString());

        }catch (Exception ex){

            logger.error("ServiceException信息："+ex.toString());

        }finally {
            mnsClient.close();
        }
    }

}
