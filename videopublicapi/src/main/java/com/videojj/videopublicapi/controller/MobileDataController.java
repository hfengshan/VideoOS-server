package com.videojj.videopublicapi.controller;

import com.google.gson.Gson;
import com.videojj.videocommon.constant.Constants;
import com.videojj.videoservice.annotation.AesService;
import com.videojj.videoservice.annotation.ApiRequestService;
import com.videojj.videoservice.apidto.CommonResponseDTO;
import com.videojj.videoservice.apidto.MobileQueryResponseDTO;
import com.videojj.videoservice.encry.CommonAesService;
import com.videojj.videoservice.encry.CommonRSAService;
import com.videojj.videoservice.entity.TbMobileData;
import com.videojj.videoservice.service.ApiService;
import com.videojj.videoservice.util.EncryUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by zhangzhewen on 2018/10/25 上午10:04.
 */
@Controller
@RequestMapping("/videoos-api")
public class MobileDataController extends AbstractController{

    @Resource
    private ApiService apiService;

    private static Logger log = LoggerFactory.getLogger(MobileDataController.class);

    private static Gson gson = new Gson();

    @AesService
    @RequestMapping(value = "/api/mobileModify", method = RequestMethod.POST)
    public @ResponseBody
    String mobileModify(@RequestAttribute(name="data") JSONObject requestParam) {

        JSONObject businessParam = null;
        try {
            businessParam = JSONObject.parseObject(requestParam.get(REQUEST_FIELD_BUSINESS_PARAM).toString());
        }catch(Exception e){
            log.error("MobileDataController.mobileModify ==> Line 55 occur error!", e);
            return returnFailJsonString("JSON字符串格式有误!");
        }
        if (null == businessParam) {

            return returnFailJsonString("没有传递参数businessParam");
        }

        String userId = businessParam.get(REQUEST_FIELD_USER_ID).toString();
        Integer creativeId = Integer.parseInt(businessParam.get(REQUEST_FIELD_CREATIVE_ID).toString());
        String businessInfo = businessParam.get(REQUEST_FIELD_BUSINESS_INFO).toString();
        String extraInfo = null;
        if(null != businessParam.get(REQUEST_FIELD_EXTRA_INFO)) {
            extraInfo = businessParam.get(REQUEST_FIELD_EXTRA_INFO).toString();
        }

        log.info("MobileDataController.mobileModify ==> req is userId:{},creativeId:{},businessInfo:{},extraInfo:{}"
                , userId, creativeId, businessInfo, extraInfo);

        try {
            apiService.mobileModify(userId, creativeId, businessInfo, extraInfo);
        } catch (Exception e) {
            log.error("MobileDataController.mobileModify ==> Line 77 occur error!", e);
            return returnFailJsonString("服务器端发生错误，请联系技术人员!");
        }

        CommonResponseDTO resDTO = new CommonResponseDTO();
        resDTO.setResCode(Constants.SUCESSCODE);
        resDTO.setResMsg(Constants.COMMONSUCCESSMSG);

        log.info("MobileDataController.mobileModify ==> res is {}", gson.toJson(resDTO));

        return commonAesService.encryResult(resDTO);

    }

    @AesService
    @RequestMapping(value = "/api/mobileQuery", method = RequestMethod.POST)
    public @ResponseBody
    String mobileQuery(@RequestAttribute(name="data") JSONObject requestParam) {

        JSONObject businessParam = null;
        try {
            businessParam = JSONObject.parseObject(requestParam.get(REQUEST_FIELD_BUSINESS_PARAM).toString());
        }catch(Exception e){
            log.error("MobileDataController.mobileQuery ==> Line 99 occur error!", e);
            return returnFailJsonString("JSON字符串格式有误!");
        }
        if (null == businessParam) {

            return returnFailJsonString("没有传递参数businessParam");
        }

        String userId = businessParam.get(REQUEST_FIELD_USER_ID).toString();
        Integer creativeId = Integer.parseInt(businessParam.get(REQUEST_FIELD_CREATIVE_ID).toString());

        String extraInfo = null;

        if(null != businessParam.get(REQUEST_FIELD_EXTRA_INFO)){

            extraInfo = businessParam.get(REQUEST_FIELD_EXTRA_INFO).toString();
        }

        log.info("MobileDataController.mobileQuery ==> req is userId:{},creativeId:{},extraInfo:{}"
                , userId, creativeId,  extraInfo);

        TbMobileData tbMobileData;
        try {
            tbMobileData = apiService.mobileQuery(userId, creativeId, extraInfo);
        } catch (Exception e) {
            log.error("MobileDataController.mobileQuery ==> Line 124 occur error!", e);
            return returnFailJsonString("服务器端发生错误，请联系技术人员!");
        }

        MobileQueryResponseDTO resDTO = new MobileQueryResponseDTO();

        if(null != tbMobileData) {
            if(StringUtils.isNotEmpty(tbMobileData.getBusinessInfo())) {
                resDTO.setBusinessInfo(JSONObject.parseObject(tbMobileData.getBusinessInfo()));
            }
            resDTO.setExtraInfo(tbMobileData.getExtraInfo());
        }
        resDTO.setResCode(Constants.SUCESSCODE);
        resDTO.setResMsg(Constants.COMMONSUCCESSMSG);

        log.info("MobileDataController.mobileQuery ==> res is {}", gson.toJson(resDTO));

        return commonAesService.encryResult(resDTO);

    }

    @AesService
    @RequestMapping(value = "/api/commonQuery", method = RequestMethod.POST)
    public @ResponseBody
    String commonQuery(@RequestAttribute(name="data") JSONObject requestParam) throws Exception{

        JSONObject data = JSONObject.parseObject(requestParam.get(REQUEST_FIELD_BUSINESS_PARAM).toString());

        Integer creativeId = Integer.parseInt(data.get(REQUEST_FIELD_CREATIVE_ID).toString());

        String businessInfo = data.get(REQUEST_FIELD_BUSINESS_INFO).toString();

        com.alibaba.fastjson.JSONObject queryResult = null;

        try {

            queryResult = apiService.queryInfoByCondition(creativeId, businessInfo);
        }catch(Exception e){

            log.error("MobileDataController.commonQuery ==> query by condition error!!",e);

            return returnFailJsonString("服务器端发生错误，请联系技术人员!");
        }
        return commonAesService.encryResult(queryResult);

    }


}
