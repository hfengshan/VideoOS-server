package com.videojj.videoservice.mns.impl;


import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudTopic;
import com.aliyun.mns.client.MNSClient;
import com.videojj.videoservice.config.MnsProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class MnsVideoTopicClient extends AbstractComponentClient {

    @Resource
    private MnsProperties mnsVideoTopicProperties;

    //    @Bean(name = "bannerTemplateCloudQueue")
    @Override
    public CloudTopic produceCloudTopic() {

        MNSClient client = new CloudAccount(

                //主题
                mnsVideoTopicProperties.getTopicName(),
                mnsVideoTopicProperties.getMnsAccessKeyId(),
                mnsVideoTopicProperties.getMnsAccessKeySecret(),
                mnsVideoTopicProperties.getMnsAccountEndPoint()).getMNSClient();
        this.client = client;
//        return client.getQueueRef(mnsVideoTopicProperties.getQueueName());
        return  client.getTopicRef(mnsVideoTopicProperties.getTopicName());

    }
}