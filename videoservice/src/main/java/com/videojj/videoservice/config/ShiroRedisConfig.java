package com.videojj.videoservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author @videopls.com
 * Created by  on 2018/7/23 下午4:14.
 * @Description:
 */
@ConfigurationProperties(prefix = "redis.shiro")
public class ShiroRedisConfig {

    private String host ;
    private int port ;
    private int expire ;
    private int timeout;
    private String password ;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
