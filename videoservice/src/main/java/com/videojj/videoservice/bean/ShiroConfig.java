package com.videojj.videoservice.bean;

import com.videojj.videocommon.exception.TvExceptionHandler;
import com.videojj.videoservice.config.ShiroRedisConfig;
import com.videojj.videoservice.config.TvSessionManager;
import com.videojj.videoservice.dao.RedisSessionDao;
import com.videojj.videoservice.encry.CommonRSAService;
import com.videojj.videoservice.filter.DataExchangeFilter;
import com.videojj.videoservice.realm.TvShiroRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.HandlerExceptionResolver;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;


/**
 *
 */
@Configuration
@EnableConfigurationProperties(ShiroRedisConfig.class)
public class ShiroConfig {

    @Resource
    private ShiroRedisConfig shiroRedisConfig;

    @Resource
    private RedisSessionDao redisSessionDao;

    /**
     * 凭证匹配器
     * （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
     * ）
     *
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashIterations(2);//散列的次数，比如散列两次，相当于 md5(md5(""));
        return hashedCredentialsMatcher;
    }

    @Bean
    @Primary
    public TvShiroRealm tvShiroRealm() {
        TvShiroRealm tvShiroRealm = new TvShiroRealm();
//        tvShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return tvShiroRealm;
    }


    @Bean
    @Primary
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(tvShiroRealm());
        // 自定义session管理 使用redis
        securityManager.setSessionManager(sessionManager());
        // 自定义缓存实现 使用redis
        securityManager.setCacheManager(cacheManager());

        SecurityUtils.setSecurityManager(securityManager);

        return securityManager;
    }

    //自定义sessionManager
    @Bean
    @Primary
    public SessionManager sessionManager() {
        TvSessionManager tvSessionManager = new TvSessionManager();
        tvSessionManager.setSessionDAO(redisSessionDao);

        SimpleCookie simplCookie = new SimpleCookie();

        simplCookie.setName("JESSID-videojj-platform");

        tvSessionManager.setSessionIdCookie(simplCookie);
        return tvSessionManager;
    }

    /**
     * 配置shiro redisManager
     * <p>
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    @Bean
    @Primary
    public RedisManager redisManager() {

        RedisManager redisManager = new RedisManager();

        redisManager.setHost(shiroRedisConfig.getHost());

        redisManager.setPassword(shiroRedisConfig.getPassword());

        redisManager.setPort(shiroRedisConfig.getPort());

        redisManager.setExpire(shiroRedisConfig.getExpire());

        redisManager.setTimeout(shiroRedisConfig.getTimeout());

        redisManager.init();

        return redisManager;
    }

    @Bean
    @Primary
    public JedisPool jedisPool(){
        JedisPool jedisPool = new JedisPool(
                new JedisPoolConfig(),
                shiroRedisConfig.getHost(),
                shiroRedisConfig.getPort(),
                shiroRedisConfig.getTimeout(),
                shiroRedisConfig.getPassword()
                );
        return jedisPool;
    }

    /**
     * cacheManager 缓存 redis实现
     * <p>
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    @Bean
    @Primary
    public RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }

    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     *
     * @param securityManager
     * @return
     */
//    @Bean
//    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
//        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
//        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
//        return authorizationAttributeSourceAdvisor;
//    }

    /**
     * 注册全局异常处理
     * @return
     */
    @Bean(name = "exceptionHandler")
    public HandlerExceptionResolver handlerExceptionResolver() {
        return new TvExceptionHandler();
    }


    @Bean
    public CommonRSAService produceCommonRSA(){

        CommonRSAService commonRSAService = new CommonRSAService();

        commonRSAService.setPublicKeyPath("public.txt");

        commonRSAService.setPrivateKeyPath("private.txt");

        commonRSAService.setPrivateKeyPwd("123456");

        return commonRSAService;

    }

    @Bean
    public FilterRegistrationBean filterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new DataExchangeFilter());//添加过滤器
        registration.addUrlPatterns("/*");//设置过滤路径，/*所有路径
        registration.addInitParameter("name", "alue");//添加默认参数
        registration.setName("MyFilter");//设置优先级
        registration.setOrder(1);//设置优先级
        return registration;
    }
}
