package com.videojj.videoportal.controller;

import com.videojj.videoservice.dto.UserPageInfoResponseDTO;
import com.videojj.videoservice.encry.CommonAesService;
import com.videojj.videoservice.encry.CommonRSAService;
import com.videojj.videoservice.encry.RSA;
import com.videojj.videoservice.handler.SendEmqService;
import com.videojj.videoservice.service.UserService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

/**
 * @Author @videopls.com
 * Created by  on 2018/6/12 下午4:24.
 * @Description:
 */
@Controller
public class IndexController {

    @Resource
    private UserService userService;

    @Resource
    private CommonRSAService commonRSAService;

    @Resource
    private SendEmqService sendEmqService;

    @Resource
    private CommonAesService aesService;

    @Resource
    private RabbitTemplate myRabbitmqTemplate;

    @RequestMapping("/videoos/index")
    public @ResponseBody String hello(){

//        myRabbitmqTemplate.convertAndSend("fanoutExchange","","ceshiyixia");

        return "hello";
    }

    @RequestMapping("/videoos/decry")
    public @ResponseBody String decry(@RequestBody Map<String,Object> data){

        String encryData = data.get("data").toString();

        String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIGXF1B74HdsIvf6SbfUAEHbexdQ" +
                "HQDejAQGh1aU96KSB8TD8l9tle6IoWgioBuObOkO8ATg9gBTjAGMTTXBE0uDgosnKisYuTHojqAc" +
                "TruZmouvJ6nFl3B3wzDNAKyg3f48BBhQnPngWVzp7XiX8tUPKCDD8c384P2JcLTbshUNAgMBAAEC" +
                "gYB07+RH6iCZYukDH8VZrmI1C1q9pLsnR6rNDx8dD/uNpXyBH9gcKIT85CnSEDyt4Q2CGMqZfG4/" +
                "ySQzWWlqsjk3xBfC7AesK5xG4h15pV309z92vz+Etwklt+i5ASQ8RFx7smh10GEJt8fLJcK5q0pk" +
                "FtVau+D6Xj31VXGGPKQ9uQJBANXLQaaofWVemKf48+/JJP5mXIbNG7r+7lp2s+7oRIrNRljEHDiM" +
                "kdFB/wfV73Agg0wB4e4gyclSAez9opQCQGMCQQCbLFXFV2tZ2Tcxe+q45lY7HmAdKrQtHVlXN4yo" +
                "6g5EuiZJesUJNCmU0gpWkJK2rqkzLsIC97uO0W7C50Jj5nfPAkAUGgJYOytfJp/P21aESzNRQCqe" +
                "+hYLZQDgPJz6b5hSPK7KldXZc04akMv73p+euU/fC6ZEj4ypgy5vLAHnTLblAkAR6DThuCvoS3vm" +
                "FwHIYrRy0BeRZhsy1Z84xG1UFOaeocjHVAanAEEILpEI2lhEArX99RholUQswBiDH4L22mLbAkEA" +
                "sF8if5EhzG6n8CohfT1xR+tJBWxoH+LJpsfp7VqfbP0KDHJvgZFYXS5cJnEuTbSEM5Wz9BygEhBT" +
                "qV+aayg3+w==";

        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCBlxdQe+B3bCL3+km31ABB23sXUB0A3owEBodW" +
                "lPeikgfEw/JfbZXuiKFoIqAbjmzpDvAE4PYAU4wBjE01wRNLg4KLJyorGLkx6I6gHE67mZqLryep" +
                "xZdwd8MwzQCsoN3+PAQYUJz54Flc6e14l/LVDyggw/HN/OD9iXC027IVDQIDAQAB";

        RSA rsa = new RSA(publicKey, privateKey);

        String mingwen1 = rsa.decryptByRSA1(encryData,"utf-8");

        System.out.println("测试");

        return mingwen1;
    }

    @RequestMapping("/videoos/aesdecry")
    public @ResponseBody String aesdecry(@RequestBody Map<String,Object> data){

        String paramStr = aesService.decryParam(data);

        System.out.println("测试");

        return paramStr;
    }


}
