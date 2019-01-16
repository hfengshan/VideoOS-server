package com.videojj.videopublicapi.controller;

import com.videojj.videoservice.apidto.LaunchApiQueryInfoResponseDTO;
import com.videojj.videoservice.service.ApiService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @Author @videopls.com
 * Created by  on 2018/6/12 下午4:24.
 * @Description:
 */
@Controller
public class IndexController {


    @Resource
    private ApiService apiService;

    @RequestMapping("/videoos-api/index")
    public @ResponseBody String hello(){

//        LaunchApiQueryInfoResponseDTO resDTO = apiService.queryInfoByVideoId("http://qa-video.oss-cn-beijing.aliyuncs.com/mp4/mby02.mp4");

        return "hello";
    }



}
