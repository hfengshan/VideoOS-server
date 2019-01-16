package com.videojj.videopublicapi.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.videojj.videocommon.constant.Constants;
import com.videojj.videocommon.util.DateUtil;
import com.videojj.videoservice.annotation.AesService;
import com.videojj.videoservice.apidto.ConfigResponseDTO;
import com.videojj.videoservice.apidto.LaunchApiQueryInfoResponseDTO;
import com.videojj.videoservice.apidto.PreloadLaunchInfoResponseDTO;
import com.videojj.videoservice.cache.FileVersionCache;
import com.videojj.videoservice.config.CommonConfig;
import com.videojj.videoservice.config.MqttProperties;
import com.videojj.videoservice.dto.NewVersionInfoResponseDTO;
import com.videojj.videoservice.service.ApiService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/22 上午10:04.
 * @Description:
 */
@Controller
@RequestMapping("/videoos-api" )
public class ApiController extends AbstractController{

    @Resource
    private ApiService apiService;

    @Resource
    private CommonConfig commonConfig;

    private static Logger log = LoggerFactory.getLogger("ApiController");

    private static Gson gson = new Gson();

    @Resource
    private MqttProperties mqttProperties;

    @Resource
    private FileVersionCache fileVersionCache;

    @AesService
    @RequestMapping(value = "/api/fileVersion", method = RequestMethod.POST)
    public @ResponseBody
    String getNewestVersion() {

        String newVersion = null;

        try {

            newVersion = fileVersionCache.getNewVersion();
        }catch (Exception e){

            log.error("ApiController.getNewestVersion ==> get new version error!!",e);

            return returnFailJsonString("查询最新版本号失败，联系相关技术人员！！");
        }
        NewVersionInfoResponseDTO res = new NewVersionInfoResponseDTO();

        if(StringUtils.isNotBlank(newVersion)){

            res.setDownloadUrl(commonConfig.getFileDomainName().concat(commonConfig.getPreKey()).concat(newVersion.concat("-template.zip")));

            res.setVersion(newVersion);

        }

        res.setResCode(Constants.SUCESSCODE);

        res.setResMsg(Constants.COMMONSUCCESSMSG);

        log.info("ApiController.getNewestVersion ==> res is {}",gson.toJson(res));

        return commonAesService.encryResult(res);
    }

    @AesService
    @RequestMapping(value = "/api/config", method = RequestMethod.POST)
    public @ResponseBody
    String queryConfig() {

        ConfigResponseDTO res = new ConfigResponseDTO();

        ConfigResponseDTO.EmqConfig emqConfig = new ConfigResponseDTO.EmqConfig();

        emqConfig.setUsername(mqttProperties.getUsername());
        emqConfig.setPassword(mqttProperties.getPassword());

        emqConfig.setHost(mqttProperties.getNameHost());

        emqConfig.setPort(mqttProperties.getHostUrl().split(":")[2]);

        res.setEmqConfig(emqConfig);

        res.setResCode(Constants.SUCESSCODE);

        res.setResMsg(Constants.COMMONSUCCESSMSG);

        log.info("ApiController.queryConfig ==> res is {}",gson.toJson(res));

        return commonAesService.encryResult(res);
    }

    @AesService
    @RequestMapping(value = "/simulation/queryInfo", method = RequestMethod.POST)
    public @ResponseBody
    String querySimulation(@RequestAttribute(name="data") JSONObject requestParam) {

        if(null == requestParam.get("creativeName")){

            LaunchApiQueryInfoResponseDTO resDTO = new LaunchApiQueryInfoResponseDTO();

            resDTO.setResCode(Constants.SUCESSCODE);

            resDTO.setResMsg(Constants.COMMONSUCCESSMSG);

            return commonAesService.encryResult(resDTO);
        }

        String creativeName = requestParam.get(REQUEST_FIELD_CREATIVE_NAME).toString();

        log.info("ApiController.querySimulation ==> req is {}",creativeName);

        LaunchApiQueryInfoResponseDTO resDTO = null;

        try {

            resDTO = apiService.queryInfoByCreativeName(creativeName);
        }catch (Exception e){

            log.error("ApiController.querySimulation ==> simulation query error!!",e);

            return returnFailJsonString("查询模拟信息报错，联系相关技术人员！！");
        }
        resDTO.setResCode(Constants.SUCESSCODE);

        resDTO.setResMsg(Constants.COMMONSUCCESSMSG);

        log.info("ApiController.querySimulation ==> res is {}",gson.toJson(resDTO));

        return commonAesService.encryResult(resDTO);

    }

    private String checkVideoId(JSONObject requestParam){

        if(null == requestParam.get(REQUEST_FIELD_VIDEO_ID)){

            LaunchApiQueryInfoResponseDTO resDTO = new LaunchApiQueryInfoResponseDTO();

            resDTO.setResCode(Constants.FAILCODE);

            resDTO.setResMsg("没有传递"+REQUEST_FIELD_VIDEO_ID);

            return commonAesService.encryResult(resDTO);
        }
        return null;
    }

    @AesService
    @RequestMapping(value = "/api/preloadLaunchInfo", method = RequestMethod.POST)
    public @ResponseBody
    String preloadLaunchInfo(@RequestAttribute(name="data") JSONObject requestParam) {

        String checkVideoIdResult = checkVideoId(requestParam);

        if(null != checkVideoIdResult){
            return checkVideoIdResult;
        }

        String videoId = requestParam.get(REQUEST_FIELD_VIDEO_ID).toString();

        log.info("ApiController.preloadLauchInfo ==> req is {}",videoId);

        PreloadLaunchInfoResponseDTO resDTO = null;
        try {
            List<String> fileUrlList = apiService.preloadLaunchInfoByVideoId(videoId);
            resDTO = new PreloadLaunchInfoResponseDTO(Constants.COMMONSUCCESSMSG,
                    null, Constants.SUCESSCODE, CollectionUtils.isEmpty(fileUrlList)?null:fileUrlList);

        }catch (Exception e){

            log.error("ApiController.preloadLauchInfo ==> preload error!!!",e);

            return returnFailJsonString("查询预加载素材信息报错，联系相关技术人员！！");
        }
        log.info("ApiController.preloadLauchInfo ==> res is {}",gson.toJson(resDTO));

        return commonAesService.encryResult(resDTO);
    }
    /**如果是aes加密的话，不再通过拦截器，拦截器会拷贝请求数据，耗费性能*/
    @RequestMapping("/api/queryLaunchInfo")
    public @ResponseBody String queryLaunchInfoAes(@RequestBody Map<String,Object> param){

        String paramStr = commonAesService.decryParam(param);

        JSONObject paramJson = JSON.parseObject(paramStr);
        String checkVideoIdResult = checkVideoId(paramJson);

        if(null != checkVideoIdResult){
            return checkVideoIdResult;
        }
        String videoId = paramJson.get(REQUEST_FIELD_VIDEO_ID).toString();

        String pre = DateUtil.toShortDateString(new Date());

        LaunchApiQueryInfoResponseDTO resDTO = null;

        try {

            resDTO = apiService.queryInfoByVideoId(pre.concat(videoId));
        }catch (Exception e){

            log.error("ApiController.queryLaunchInfoAes ==> query error!!",e);

            return returnFailJsonString("查询投放信息报错，联系相关技术人员！！");

        }
        log.info("ApiController.queryLaunchInfoAes ==> result is {}",resDTO.getLaunchInfoList());

        resDTO.setResCode(Constants.SUCESSCODE);

        resDTO.setResMsg(Constants.COMMONSUCCESSMSG);

        return commonAesService.encryResult(resDTO);

    }

}
