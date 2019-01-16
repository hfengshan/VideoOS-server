package com.videojj.videoservice.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.MultipartConfigElement;
import javax.sql.DataSource;

/**
 * @Author @videopls.com
 * Created by  on 2018/7/4 下午8:04.
 * @Description:
 */
@Configuration
public class CrosFilterBean {

    @Bean
    @Primary
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }

    /**
     * 文件上传配置
     * @return
     */
    @Bean
    @Primary
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //文件最大
        factory.setMaxFileSize("9000MB"); //KB,MB
        /// 设置总上传数据总大小
        factory.setMaxRequestSize("9000MB");
        return factory.createMultipartConfig();
    }


}
