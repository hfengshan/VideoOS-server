package com.videojj.videoservice.filter;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/22 下午6:28.
 * @Description:
 */
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.videojj.videoservice.bean.BodyReaderHttpServletRequestWrapper;

public class DataExchangeFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {


        ServletRequest requestWrapper = null;
        if(request instanceof HttpServletRequest) {
            if(((HttpServletRequest) request).getMethod().equals("POST")
                    &&(!((HttpServletRequest) request).getHeader("content-Type").contains("multipart/form-data"))) {

                requestWrapper = new BodyReaderHttpServletRequestWrapper((HttpServletRequest) request);

            }
        }
        if(null == requestWrapper) {
            chain.doFilter(request, response);
        } else {
            chain.doFilter(requestWrapper, response);
        }

    }

    @Override
    public void destroy() {

    }

}