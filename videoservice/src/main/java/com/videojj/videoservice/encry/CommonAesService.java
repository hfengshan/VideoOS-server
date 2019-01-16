package com.videojj.videoservice.encry;

/**
 * @Author @videopls.com
 * Created by on 2018/12/12 下午5:09.
 * @Description:
 */

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.videojj.videoservice.apidto.LaunchApiQueryInfoResponseDTO;
import com.videojj.videoservice.config.CommonConfig;
import com.videojj.videoservice.util.AesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
public class CommonAesService {

    @Resource
    private CommonConfig commonConfig;

    private static Gson gson = new GsonBuilder().disableHtmlEscaping().serializeNulls().create();


    private static Logger log = LoggerFactory.getLogger("commonRequestLog");

    public String decryParam(Map<String, Object> param) {

        String data = param.get("data").toString();

        log.info("encry data is {}",data);

        String paramStr = AesUtils.decrypt4Aes2Str(commonConfig.getAesKey(),data);

        log.info("request commonParam ==> {}",paramStr);

        return paramStr;

    }
    public String decryStr(String data) {

        String paramStr = AesUtils.decrypt4Aes2Str(commonConfig.getAesKey(),data);

        return paramStr;

    }


    public <T> String encryResult(T resDTO) {

        String encryData = AesUtils.encrypt4Aes(commonConfig.getAesKey(), JSON.toJSONString(resDTO));

        Map<String,Object> returnDataMap = new HashMap<>();

        returnDataMap.put("encryptData",encryData);

//        return JSON.toJSONString(returnDataMap);

        return gson.toJson(returnDataMap);

    }
}
