package com.videojj.videoservice.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix="mns.common.property")
public class MnsProperties extends MnsConfig {

    @Value("${mns.common.property.topicName}")
    protected String topicName;

    @Value("${mns.common.property.mnsAccessKeyId}")
    protected String mnsAccessKeyId;

    @Value("${mns.common.property.mnsAccessKeySecret}")
    protected String mnsAccessKeySecret;

    @Value("${mns.common.property.mnsAccountEndPoint}")
    protected String mnsAccountEndPoint;


}
