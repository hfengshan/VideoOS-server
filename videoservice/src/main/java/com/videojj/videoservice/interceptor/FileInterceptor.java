package com.videojj.videoservice.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.videojj.videocommon.constant.Constants;
import com.videojj.videocommon.dto.base.BaseResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author @videopls.com
 * Created by  on 2018/10/25 下午6:44.
 * @Description:
 */
public class FileInterceptor implements HandlerInterceptor {

    private static Logger log = LoggerFactory.getLogger("FileInterceptor");

    private final String imgSuffix = "jpg,png,jpeg";

    private final String gifSuffix = "gif";

    private final String videoSuffix = "mp4";

    /**100K*/
    private final long imgMaxSize = 102400l;

    private final long gifMaxSize = 2097152l;

    /**20MB*/
    private final long videoMaxSize = 20971520l;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{

        /**如果是文件操作，那么就要判断具体的文件的格式类型了*/
        if (request instanceof MultipartHttpServletRequest) {

            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

            Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

            if(null == fileMap){

                return true;
            }

            Iterator<String> keyIter = fileMap.keySet().iterator();

            String key = null;

            if(keyIter.hasNext()) {

                key = keyIter.next();
            }else{

                return true;
            }
            MultipartFile multipartFile =
                    multipartRequest.getFile(key);

            String fileName=multipartFile.getOriginalFilename();

            String suffix = fileName.substring(fileName.lastIndexOf(".")
                    + 1, fileName.length());

            long size = multipartFile.getSize();

            if (imgSuffix.contains(suffix.trim().toLowerCase())) {
                /**图片文件*/
                if(size>imgMaxSize){
                    response.setCharacterEncoding("utf-8");

                    response.setContentType("multipart/form-data;charset=UTF-8");

                    BaseResponseDTO baseResponseDTO = new BaseResponseDTO();

                    baseResponseDTO.setResCode(Constants.FAILCODE);

                    baseResponseDTO.setResMsg("文件大小超过了100KB,请选择小一点的图片");

                    response.getWriter().write(JSONObject.toJSONString(baseResponseDTO));

                    response.getWriter().flush();

                    return false;
                }

            }else if(videoSuffix.contains(suffix.trim().toLowerCase())){
                /**视频文件*/
                if(size>videoMaxSize){
                    response.setCharacterEncoding("utf-8");

                    response.setContentType("multipart/form-data;charset=UTF-8");

                    BaseResponseDTO baseResponseDTO = new BaseResponseDTO();

                    baseResponseDTO.setResCode(Constants.FAILCODE);

                    baseResponseDTO.setResMsg("文件大小超过了20MB，请选择小一点的视频");

                    response.getWriter().write(JSONObject.toJSONString(baseResponseDTO));

                    response.getWriter().flush();

                    return false;

                }

            }else if(gifSuffix.contains(suffix.trim().toLowerCase())){

                if(size>gifMaxSize){

                    response.setCharacterEncoding("utf-8");

                    response.setContentType("multipart/form-data;charset=UTF-8");

                    BaseResponseDTO baseResponseDTO = new BaseResponseDTO();

                    baseResponseDTO.setResCode(Constants.FAILCODE);

                    baseResponseDTO.setResMsg("文件大小超过了2MB,请选择小一点的图片");

                    response.getWriter().write(JSONObject.toJSONString(baseResponseDTO));

                    response.getWriter().flush();

                    return false;

                }


            }else{

                return true;
            }

        }

        return true;
    }


}
