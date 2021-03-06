package com.videojj.videoservice.interceptor;

import com.videojj.videocommon.constant.Constants;
import com.videojj.videocommon.dto.base.BaseResponseDTO;
import com.videojj.videocommon.exception.ServiceException;
import com.videojj.videoservice.annotation.AesService;
import com.videojj.videoservice.annotation.ApiRequestService;
import com.videojj.videoservice.bean.BodyReaderHttpServletRequestWrapper;
import com.videojj.videoservice.encry.CommonAesService;
import com.videojj.videoservice.encry.CommonRSAService;
import com.videojj.videoservice.util.EncryUtil;
import com.videojj.videoservice.util.HttpBodyUtil;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import com.alibaba.fastjson.JSONObject;


/**
 * @Author @videopls.com
 * Created by  on 2018/8/22 下午3:23.
 * @Description:
 */
public class AesServiceInterceptor implements HandlerInterceptor {

    private static Logger log = LoggerFactory.getLogger("commonRequestLog");

    private CommonAesService commonAesService;

    public AesServiceInterceptor(CommonAesService commonAesService){

        this.commonAesService = commonAesService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!(handler instanceof HandlerMethod)){
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        AesService apiRequestService = handlerMethod.getMethodAnnotation(AesService.class);
        if (apiRequestService == null) {

            return true;
        }
        String bodyData = HttpBodyUtil.getBodyData(request);
        if (Base64.isBase64(bodyData)) {
            bodyData = new String(Base64.decodeBase64(bodyData), StandardCharsets.UTF_8);
        }

        JSONObject requestParam = JSONObject.parseObject(bodyData);

        if(null == requestParam.get("data")){

            response.setCharacterEncoding("utf-8");

            response.setContentType("application/json;charset=UTF-8");

            BaseResponseDTO baseResponseDTO = new BaseResponseDTO();

            baseResponseDTO.setResCode(Constants.FAILCODE);

            baseResponseDTO.setResMsg("data 为空");

            response.getWriter().write(commonAesService.encryResult(baseResponseDTO));

            response.getWriter().flush();

            return false;
        }

        String bodyDataStr = requestParam.get("data").toString();

        String decryBodyData = commonAesService.decryStr(bodyDataStr);

        log.info("{} ==>decrypt result is {}",handlerMethod.getMethod().toString(),decryBodyData);

        JSONObject requestJsonParam = JSONObject.parseObject(decryBodyData);

        request.setAttribute("data",requestJsonParam);

        if(null == requestJsonParam.get("commonParam")){

            response.setCharacterEncoding("utf-8");

            response.setContentType("application/json;charset=UTF-8");

            BaseResponseDTO baseResponseDTO = new BaseResponseDTO();

            baseResponseDTO.setResCode(Constants.FAILCODE);

            baseResponseDTO.setResMsg("缺少commonParam字段");

            response.getWriter().write(commonAesService.encryResult(baseResponseDTO));

            response.getWriter().flush();

            return false;
        }

        log.info("request commonParam ==> {}",decryBodyData);

        return true;
    }



}
