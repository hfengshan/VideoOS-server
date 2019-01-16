package com.videojj.videoservice.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.videojj.videocommon.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author @videopls.com
 * Created by  on 2018/11/2 下午4:24.
 * @Description:
 */
public class SqlUtil {

    private static String originCountSql= "select count(*) from tb_mobile_data_detail where ";


    public static String generateSqlString(String dataString) {

//        JSONArray jsonArray = JSONArray.parseArray(dataString);

        String originSql = null;

//        int size = jsonArray.size();

//        for(int index = 0;index<size;index++){
//
//            String conditionAndData = jsonArray.get(index).toString();

        JSONObject cdJson = JSONObject.parseObject(dataString);

        if(null == cdJson.get("action")|| null == cdJson.get("condition")){

            throw new ServiceException("数据格式有问题，请check");
        }

        String action = cdJson.get("action").toString();

        originSql = getOriginSql(action);

        if(StringUtils.isEmpty(originSql)){

            throw new ServiceException("数据格式，action有问题");
        }
        JSONArray conditionArray = JSONObject.parseArray(cdJson.get("condition").toString());

        int conditionSize = conditionArray.size();

        for(int conIndex = 0;conIndex<conditionSize;conIndex++){

            JSONObject condition = JSONObject.parseObject(conditionArray.get(conIndex).toString());

            if(conIndex != 0){

                originSql = originSql+" and ";
            }

            String oper = generateOperString(condition.get("operator").toString());

            String key = condition.get("key").toString();

            String value = condition.get("value").toString();

            originSql = originSql + " data_key"+oper +"'"+ key +"' and data_value "+ oper+"'" +value+"'" ;

        }

//        }

        return originSql;
    }

    private static String generateOperString(String operator) {

        if("equal".equals(operator)){

            return " = ";
        }else{

            return null;
        }

    }

    private static String getOriginSql(String action) {

        if("count".equals(action)){

            return originCountSql;
        }else{

            return null;
        }

    }
}
