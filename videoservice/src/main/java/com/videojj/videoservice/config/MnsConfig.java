package com.videojj.videoservice.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix ="mns.common.property")
@Component
public class MnsConfig {

    protected  String topicName;

    protected String mnsAccessKeyId;

    protected String mnsAccessKeySecret;

    protected String mnsAccountEndPoint;

    public String getTopicName(){
        return topicName;
    }

    public void  setTopicName(String topicName){
        this.topicName = topicName;
    }

    public String getMnsAccessKeyId() {
        return mnsAccessKeyId;
    }

    public void setMnsAccessKeyId(String mnsAccessKeyId) {
        this.mnsAccessKeyId = mnsAccessKeyId;
    }

    public String getMnsAccessKeySecret() {
        return mnsAccessKeySecret;
    }

    public void setMnsAccessKeySecret(String mnsAccessKeySecret) {
        this.mnsAccessKeySecret = mnsAccessKeySecret;
    }

    public String getMnsAccountEndPoint() {
        return mnsAccountEndPoint;
    }

    public void setMnsAccountEndPoint(String mnsAccountEndPoint) {
        this.mnsAccountEndPoint = mnsAccountEndPoint;
    }

}

