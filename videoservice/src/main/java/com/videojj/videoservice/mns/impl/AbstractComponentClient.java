package com.videojj.videoservice.mns.impl;

import com.aliyun.mns.client.CloudTopic;
import com.aliyun.mns.client.MNSClient;

import javax.annotation.PreDestroy;

public abstract class AbstractComponentClient {

    protected MNSClient client;

    public abstract CloudTopic produceCloudTopic();

    @PreDestroy
    public void destroy() {

        client.close();
    }
}

