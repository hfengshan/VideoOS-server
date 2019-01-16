package com.videojj.videoservice.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author @videopls.com
 * Created by  on 2018/10/22 下午4:14.
 * @Description:
 */
@Getter
@Setter
@ToString
@Component
public class FileServerConfig {

    /**
     * SFTP 登录用户名
     */
    @Value("${video.sftp.username}")
    private String username;
    /**
     * SFTP 登录密码
     */
    @Value("${video.sftp.password}")
    private String password;
    /**
     * 私钥
     */
    private String privateKey;
    /**
     * SFTP 服务器地址IP地址
     */
    @Value("${video.sftp.host}")
    private String host;
    /**
     * SFTP 端口
     */
    @Value("${video.sftp.port}")
    private int port;
    /**
     * 远程文件系统的操作根路径
     */
    @Value("${video.sftp.rootPath}")
    private String rootPath;



    @Value("${video.oss.key}")
    private String accessKey;

    @Value("${video.oss.endpoint}")
    private String  endpoint;

    @Value("${video.oss.bucketName}")
    private String  bucketName;

    @Value("${video.oss.secret}")
    private String  secret;

//    @Value("${video.oss.cdn}")
//    private String cdn;

}
