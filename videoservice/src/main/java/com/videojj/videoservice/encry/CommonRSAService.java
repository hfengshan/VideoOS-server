package com.videojj.videoservice.encry;

import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;

/**
 * 类CqRSAService.java的实现描述：重庆小贷 RSA 服务类
 *
 * @author  2017/8/9 13:36.
 */
@Slf4j
public class CommonRSAService extends BaseRSAService {

    /**
     * 功能:公钥验签
     * 作者:
     * 创建日期:2017-6-9
     * @param rawData
     * @param sign
     * @param charset
     * @return
     */
    public boolean verify(String rawData, String sign, String charset) {
        return this.rsa.verify(rawData, sign, charset);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.rsa == null) {
            log.info("rsa is null, use key instance rsa");
            Assert.isTrue(StringUtils.isNotBlank(publicKeyPath), "公钥路径为空");
            Assert.isTrue(StringUtils.isNotBlank(privateKeyPath), "私钥路径为空");
            URL res = this.getClass().getClassLoader().getResource("/");
            if (res == null) {
                res = this.getClass().getClassLoader().getResource("");
            }

            ClassPathResource resource = new ClassPathResource(publicKeyPath);
            InputStream inputStream = resource.getInputStream();
            String pubKeyStr = IOUtils.toString(inputStream, "utf-8");

            ClassPathResource presource = new ClassPathResource(privateKeyPath);
            InputStream pinputStream = presource.getInputStream();
            String priKeyStr = IOUtils.toString(pinputStream, "utf-8");
            this.rsa = new RSA(pubKeyStr, priKeyStr);
        }
    }
}

