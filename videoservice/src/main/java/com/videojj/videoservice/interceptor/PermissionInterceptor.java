package com.videojj.videoservice.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.videojj.videocommon.constant.Constants;
import com.videojj.videocommon.dto.base.BaseResponseDTO;
import com.videojj.videocommon.exception.BadRequestException;
import com.videojj.videoservice.annotation.PermissionService;
import com.videojj.videoservice.dao.RedisSessionDao;
import com.videojj.videoservice.util.PermissionUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 */

public class PermissionInterceptor implements HandlerInterceptor {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    RedisSessionDao redisSessionDao;

    public PermissionInterceptor(RedisSessionDao redisSessionDao){

        this.redisSessionDao = redisSessionDao;

    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!(handler instanceof HandlerMethod)){
            return true;
        }

        //1.先判断请求url是否有 权限注解  有则代表需要做权限校验 无则直接放行
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        PermissionService permissionService = handlerMethod.getMethodAnnotation(PermissionService.class);
        if (permissionService == null) {
            return true;
        }

        //2.走到这里 说明需要对管理员进行操作权限的校验 a.查询当前用户
        try {
            //从header中获取token
            String token = request.getHeader("token");
            //如果header中不存在token，则从参数中获取token
            if(StringUtils.isBlank(token)){
                token = request.getParameter("token");
            }

            //token为空
            if(StringUtils.isBlank(token)){
                throw new BadRequestException("token不能为空");
            }

            Session session = redisSessionDao.doReadSession(token);

            if(null != session){

                String userName = session.getAttribute("org.apache.shiro.subject.support.DefaultSubjectContext_PRINCIPALS_SESSION_KEY").toString();

                request.setAttribute("username", userName);
                PermissionUtil.setCurrentUsername(userName);
            }else{

                throw new BadRequestException("token 已经过期，请重新登录");
            }

        } catch (Exception e) {
            logger.error("权限校验失败!");
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json;charset=UTF-8");

            BaseResponseDTO baseResponseDTO = new BaseResponseDTO();

            baseResponseDTO.setResCode(Constants.FAILCODE);

            baseResponseDTO.setResMsg("token过期，请重新登录");

            response.getWriter().write(JSONObject.toJSONString(baseResponseDTO));
            response.getWriter().flush();
            return false;
        }

        return true;
    }

}