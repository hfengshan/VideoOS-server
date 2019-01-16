package com.videojj.videoservice.config;

import com.videojj.videoservice.encry.CommonAesService;
import com.videojj.videoservice.encry.CommonRSAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * created by on 2018/6/10
 *
 *
 * Desc:对pc端管理员的操作权限拦截
 */
@Configuration
public class PermissionConfig implements WebMvcConfigurer {

    @Autowired
    com.videojj.videoservice.dao.RedisSessionDao redisSessionDao;

    @Autowired
    CommonRSAService commonRSAService;

    @Autowired
    CommonAesService commonAesService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加权限拦截器
        registry.addInterceptor(new com.videojj.videoservice.interceptor.PermissionInterceptor(redisSessionDao));

        registry.addInterceptor(new com.videojj.videoservice.interceptor.ApiRequestInterceptor(commonRSAService));

        registry.addInterceptor(new com.videojj.videoservice.interceptor.FileInterceptor());

        registry.addInterceptor(new com.videojj.videoservice.interceptor.PageInterceptor());

        registry.addInterceptor(new com.videojj.videoservice.interceptor.AesServiceInterceptor(commonAesService));

    }
}