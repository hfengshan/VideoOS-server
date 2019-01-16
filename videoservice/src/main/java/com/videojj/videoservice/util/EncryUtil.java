package com.videojj.videoservice.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.videojj.videoservice.encry.CommonRSAService;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/22 上午10:48.
 * @Description:
 */
public class EncryUtil {

    private static Gson gson = new GsonBuilder().disableHtmlEscaping().serializeNulls().create();

    public static <T> String signResult(T res,CommonRSAService commonRSAService) {

        String jsonString = gson.toJson(res);

        String encryData = commonRSAService.privateEncrypt(jsonString,"utf-8");

        Map<String,Object> returnDataMap = new HashMap<>();

        returnDataMap.put("encryptData",encryData);

        return gson.toJson(returnDataMap);
    }
}
