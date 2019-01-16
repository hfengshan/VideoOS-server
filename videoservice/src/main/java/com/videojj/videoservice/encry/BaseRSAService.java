/*
 * Copyright 2017 Zhongan.com All right reserved. This software is the
 * confidential and proprietary information of Zhongan.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Zhongan.com.
 */
package com.videojj.videoservice.encry;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.net.URL;

/**
 * 类BaseRSAService.java的实现描述：RSA加解密服务类
 *
 * @author  2017/8/4 14:55.
 */
@Slf4j
@Setter
public class BaseRSAService implements InitializingBean {

    /**
     * 编码格式
     */
    protected static final String CHARSET = "UTF-8";

    /**
     * 对方公钥存放路径
     */
    protected String publicKeyPath;

    /**
     * 己方私钥存放路径
     */
    protected String privateKeyPath;

    /**
     * 己方私钥证书密码
     */
    protected String privateKeyPwd;

    /**
     * RSAUtil
     */
    protected RSA rsa;

    /**
     * 请求报文加密处理
     * @param data
     * @return
     */
    public String encrypt(String data) {
        return rsa.encryptByRSA(data, CHARSET);
    }

    /**
     * 请求报文加签处理
     * @param data
     * @return
     */
    public String sign(String data) {
        return rsa.sign(data, CHARSET);
    }

    /**
     * 响应报文验签、解密处理
     * @param data
     * @param sign
     * @return
     */
    public String verifyAndDecrypt(String data, String sign) {
        boolean b = rsa.verify(data, sign, CHARSET);
        if (!b) {
            throw new RuntimeException(String.format(
                    "BaseRSAService.verifyAndDecrypt 对数据[%s]进行[%s]验签失败", data, sign));
        }
        return rsa.decryptByRSA(data, CHARSET);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.rsa == null) {
            log.info("rsa is null, use key instance rsa");
            Assert.isTrue(StringUtils.isNotBlank(publicKeyPath), "公钥路径为空");
            Assert.isTrue(StringUtils.isNotBlank(privateKeyPath), "私钥路径为空");
            Assert.isTrue(StringUtils.isNotBlank(privateKeyPwd), "私钥证书密码为空");
            URL resource = this.getClass().getClassLoader().getResource("/");
            if (resource == null) {
                resource = this.getClass().getClassLoader().getResource("");
            }
            // absolute path
            String pubKeyAP = resource.getPath().concat(publicKeyPath);
            String priKeyAP = resource.getPath().concat(privateKeyPath);
            this.rsa = new RSA(pubKeyAP, priKeyAP, privateKeyPwd);
        }
    }
    
    /**
     * 公钥验签
     * @param rawData
     * @param sign
     * @param charset
     * @return
     */
    public boolean verify(String rawData, String sign, String charset){
        return rsa.verify(rawData, sign, charset);
    }
    
    
    /**
     * 私钥解密
     * @param data
     * @param charset
     * @return
     */
    public String decrypt(String data, String charset){
        return rsa.decryptByRSA(data, charset);
    }
    /**
     * 私钥进行加密
     */
    public String privateEncrypt(String data,String charset){

        return rsa.encryptByRSA1(data,charset);
    }
    /**
     * 公钥进行解密
     */
    public String publicDecrypt(String data,String charset){

        return rsa.decryptByRSA1(data,charset);
    }

    
    /**
     * 默认返回null，待子类实现
     * @return
     */
    public String getMerchantId(){
        return null;
    }
}
