package com.videojj.videoservice.interceptor;

import com.github.pagehelper.PageHelper;
import com.videojj.videocommon.util.SqlUtil;
import com.videojj.videoservice.annotation.PageControllerService;
import jodd.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Mybatis PageHelper统一拦截器，自动获取并且设置<当前页码><每页大小>。
 *
 * @author zhangzhewen
 * @date 2018/11/26
 */
public class PageInterceptor implements HandlerInterceptor {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if(!(handler instanceof HandlerMethod)){
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        PageControllerService pageControllerService = handlerMethod.getMethodAnnotation(PageControllerService.class);
        if (pageControllerService == null) {
            return true;
        }

        String pageNumRaw = request.getParameter(pageControllerService.pageNumName());
        String pageSizeRaw = request.getParameter(pageControllerService.pageSizeName());
        String orderByRaw = request.getParameter(pageControllerService.orderByName());

        int pageNum, pageSize;
        String orderBy;

        if (StringUtils.isEmpty(pageNumRaw)) {
            pageNum = pageControllerService.pageNumDefaultValue();
        } else {
            pageNum = Integer.parseInt(pageNumRaw);
        }
        if (StringUtils.isEmpty(pageSizeRaw)) {
            pageSize = pageControllerService.pageSizeDefaultValue();
        } else {
            pageSize = Integer.parseInt(pageSizeRaw);
        }
        if (StringUtils.isEmpty(orderByRaw)) {
            orderBy = pageControllerService.orderByDefaultValue();
        } else {
            orderBy = orderByRaw;
        }
        //驼峰处理
        orderBy = StringUtil.fromCamelCase(orderBy, '_');
        //SQL注入处理
        orderBy = SqlUtil.transactSqlInjection(orderBy);
        //执行分页处理
        PageHelper.startPage(pageNum, pageSize, orderBy);
        logger.debug("{}方法执行了分页逻辑，页码:{}，页面大小:{}。", handlerMethod.getMethod().toString(), pageNum, pageSize);
        return true;
    }

}
