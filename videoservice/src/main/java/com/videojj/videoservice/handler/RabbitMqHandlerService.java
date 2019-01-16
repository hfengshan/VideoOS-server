package com.videojj.videoservice.handler;

import com.rabbitmq.client.Channel;
import com.videojj.videoservice.cache.LaunchPlanCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RabbitMqHandlerService implements ChannelAwareMessageListener {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMqHandlerService.class);

    @Resource
    private LaunchPlanCache launchPlanCache;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        byte[] body = message.getBody();
        logger.info("接收到消息:" + new String(body));

        String videoId = new String(body);

        launchPlanCache.updateLocalCache(videoId);
    }
}