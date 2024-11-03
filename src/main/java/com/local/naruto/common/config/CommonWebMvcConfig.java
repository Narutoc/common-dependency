package com.local.naruto.common.config;

import com.local.naruto.common.interceptor.LogTraceIdInterceptor;
import com.local.naruto.common.interceptor.TokenInterceptor;
import com.local.naruto.common.interceptor.UserLoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用拦截配置类
 * <p>
 * 用于自定义拦截器的配置
 *
 * @author naruto chen
 * @since 2023-12-18
 */
@Configuration
public class CommonWebMvcConfig implements WebMvcConfigurer {

    @Bean("tokenInterceptor")
    public TokenInterceptor tokenInterceptor() {
        return new TokenInterceptor();
    }

    @Bean("traceIdInterceptor")
    public LogTraceIdInterceptor traceIdInterceptor() {
        return new LogTraceIdInterceptor();
    }

    @Bean("userLoginInterceptor")
    public UserLoginInterceptor userLoginInterceptor(){
        return new UserLoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // traceId需要拦截的
        registry.addInterceptor(traceIdInterceptor())
            .addPathPatterns("/**");

        // 以下URL不在拦截范围内
        List<String> excludePath = new ArrayList<>();
        // 用户登录
        excludePath.add("/api/v1/basicdata/user/login");
        // 登录验证码
        excludePath.add("/api/v1/basicdata/code");
        // 接口文档
        excludePath.add("/api/api-docs/**");
        excludePath.add("/api/swagger/**");

        // token需要拦截的URL
        registry.addInterceptor(tokenInterceptor())
            .addPathPatterns("/**")
            .excludePathPatterns(excludePath);

        // 用户登录信息获取需要拦截的URL
        registry.addInterceptor(userLoginInterceptor())
            .addPathPatterns("/**")
            .excludePathPatterns(excludePath);

        WebMvcConfigurer.super.addInterceptors(registry);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
            .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/");
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowCredentials(true)
            .allowedOriginPatterns("*")
            .allowedHeaders("*")
            .allowedMethods("GET", "POST", "DELETE")
            .maxAge(3600);
    }
}
