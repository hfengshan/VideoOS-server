package com.videojj.videoservice.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author @videopls.com
 * Created by  on 2018/10/16 下午7:13.
 * @Description:
 */
public class JsonUtil {

    public static final String NOTEXIST = "Not Found";

    public static String getValueByKeyFromJson(Object object, String key) {
        if (object == null || object == "")
            return null;
        Object oJson = object;
        Class<? extends Object> cls = oJson.getClass();
        if (cls == JSONObject.class) {
            JSONObject jo = (JSONObject) oJson;
            if (jo.containsKey(key)) {
                return jo.getString(key);
            }
            for (Object o : jo.values()) {

                String tmp = getValueByKeyFromJson(o, key);
                if (StringUtils.isNotEmpty(tmp)&&!tmp.equals(NOTEXIST)) {
                    return tmp;
                }
            }
        } else if (cls == JSONArray.class) {

            JSONArray ja = (JSONArray) oJson;
            int size = ja.size();
            for (int i = 0; i < size; i++) {
                Object o = ja.get(i);
                if (o != null && o != "") {
                    String tmp = getValueByKeyFromJson(o, key);
                    if (StringUtils.isNotEmpty(tmp)&&!tmp.equals(NOTEXIST)) {
                        return tmp;
                    }
                }
            }
        } else if (cls == String.class) {

            Object o = null;
            try {
                o = JSON.parse((String) oJson);
                String tmp = getValueByKeyFromJson(o, key);
                if (StringUtils.isNotEmpty(tmp)&&!tmp.equals(NOTEXIST)) {
                    return tmp;
                }
            } catch (JSONException e) {
            }
        }
        return "Not Found";
    }

    public static List<String> parseLaunchTimeString(String launchTime){

        List<String> timeList = new ArrayList<>();

        JSONArray jsonArray = JSONArray.parseArray(launchTime);

        /**如果是即时投放的话，那么投放时间就是空，投放时长是一直都会有的，所以这里其实是有点问题的*/
        for(int outIndex=0;outIndex<jsonArray.size();outIndex++) {

            JSONArray innerArray = JSONArray.parseArray(jsonArray.get(outIndex).toString());

            for(int index = 0;index<innerArray.size();index++) {

                String time = innerArray.get(index).toString();

                timeList.add(time);
            }
        }
        return timeList;

    }


    public static Map<String,Object> toMap(String businessInfo) {

        Map mapType = JSON.parseObject(businessInfo,Map.class);

        return mapType;

    }
}
