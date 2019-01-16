package com.videojj.videoservice.util;

import com.videojj.videocommon.exception.ServiceException;
import com.videojj.videoservice.bean.BodyReaderHttpServletRequestWrapper;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Author wangpeng@videopls.com
 * Created by wangpeng on 2018/12/12 下午5:31.
 * @Description:
 */
public class HttpBodyUtil {


    public static String getBodyData(HttpServletRequest request) {
        ServletRequest requestWrapper = new BodyReaderHttpServletRequestWrapper((HttpServletRequest) request);


//        String requestURL = ((HttpServletRequest) requestWrapper).getRequestURL().toString();

        //获取json
//        String contentType = requestWrapper.getContentType();

        String paramJson = getJsonParam((HttpServletRequest) requestWrapper);

        return paramJson;
    }

    /**
     * 获取Json数据
     *
     * @param request
     * @return
     */
    private static String getJsonParam(HttpServletRequest request) {
        String jsonParam = "";
        ServletInputStream inputStream = null;
        try {
            int contentLength = request.getContentLength();
            if (!(contentLength < 0)) {
                byte[] buffer = new byte[contentLength];
                inputStream = request.getInputStream();
                for (int i = 0; i < contentLength; ) {
                    int len = inputStream.read(buffer, i, contentLength);
                    if (len == -1) {
                        break;
                    }
                    i += len;
                }
                jsonParam = new String(buffer, "utf-8");
            }
        } catch (IOException e) {
            throw new ServiceException("参数转换成json异常g{}", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new ServiceException("参数转换成json异常s{}", e);
                }
            }
        }
        return jsonParam;
    }

}
