package com.local.naruto.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.local.naruto.common.constant.Constants;
import com.local.naruto.common.exception.AuthenticationException;
import com.local.naruto.common.utils.SpringContextUtils;
import com.local.naruto.common.utils.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.springframework.web.servlet.HandlerInterceptor;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * token拦截器
 *
 * @author naruto chen
 * @since 2023-12-18
 */
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {

    private static final String INVALID_TOKEN = "token无效，请检查后重试";
    private static final String EXPIRED_TOKEN = "授权已过期，请重新登录获取授权";
    private static final String USER_LOGOUT = "用户未登录，请登录后获取授权";
    private static final String EMPTY_TOKEN = "token为空，请检查后重试";
    private static final String EMPTY = "用户登录信息为空，请检查";

    /**
     * 接口请求校验token是否有效
     * <p>
     * 1、如果请求头中传入token，则先校验该token
     * <p>
     * 2、再去校验请求头X-AUTH-USER传值对应账号的token
     */
    @Override
    public boolean preHandle(HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, Object handler)
        throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");

        // 方式1：请求头中传入token
        String headerToken = request.getHeader(Constants.HEADER_TOKEN);
        // 方式2：请求头中传入用户账号
        String headUserAccount = request.getHeader("X-AUTH-USER");

        log.info("X-AUTH-USER from header is : {}", headUserAccount);
        log.info("token from header is : {}", headUserAccount);


        // 都没有传，给出提示
        if (StringUtils.isEmpty(headerToken) && StringUtils.isEmpty(headUserAccount)) {
            AuthenticationException authException = new AuthenticationException(EMPTY);
            response.setStatus(HttpStatus.SC_UNAUTHORIZED);
            response.getOutputStream().write(JSON.toJSONString(authException).getBytes(StandardCharsets.UTF_8));
            return false;
        }

        // header传入了token
        if (StringUtils.isNotEmpty(headerToken)) {
            return isHeaderTokenValid(request, response);
        }

        // header没有传入token，传入了用户账号
        if (StringUtils.isEmpty(headerToken) && StringUtils.isNotEmpty(headUserAccount)) {
            return isHeaderUserValid(request, response);
        }
        return true;
    }

    /**
     * 根据账号从缓存中查询token
     *
     * @param account 账号
     * @return token
     */
    private String getCacheToken(String account) {
        RedissonClient redisBean = SpringContextUtils.getBean(RedissonClient.class);
        String cacheToken = null;
        if (StringUtils.isNotEmpty(account) && Objects.nonNull(redisBean)) {
            RBucket<String> userBucket = redisBean.getBucket(Constants.USER_TOKEN + account, JsonJacksonCodec.INSTANCE);
            cacheToken = userBucket.get();
        }
        return cacheToken;
    }

    /**
     * header请求头中传入的token是否有效
     *
     * @param request  header请求头
     * @param response response
     * @return boolean
     */
    private boolean isHeaderTokenValid(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // token无效给出提示
        String headerToken = request.getHeader("token");
        if (!TokenUtils.isValidToken(headerToken)) {
            AuthenticationException authException = new AuthenticationException(INVALID_TOKEN);
            response.setStatus(HttpStatus.SC_UNAUTHORIZED);
            response.getOutputStream().write(JSON.toJSONString(authException).getBytes(StandardCharsets.UTF_8));
            return false;
        }

        // token过期给出提示
        if (TokenUtils.isTokenExpired(headerToken)) {
            AuthenticationException authException = new AuthenticationException(EXPIRED_TOKEN);
            response.setStatus(HttpStatus.SC_UNAUTHORIZED);
            response.getOutputStream().write(JSON.toJSONString(authException).getBytes(StandardCharsets.UTF_8));
            return false;
        }

        // 用户已注销，给出提示
//        String account = TokenUtils.getUserAccountByToken(headerToken);
//        if (StringUtils.isEmpty(getCacheToken(account))) {
//            AuthenticationException authException = new AuthenticationException(USER_LOGOUT);
//            response.setStatus(HttpStatus.SC_UNAUTHORIZED);
//            response.getOutputStream().write(JSON.toJSONString(authException).getBytes(StandardCharsets.UTF_8));
//            return false;
//        }
        return true;
    }

    /**
     * header请求头中传入的用户账号是否有效
     *
     * @param request  header请求头
     * @param response response
     * @return boolean
     */
    private boolean isHeaderUserValid(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String headUserAccount = request.getHeader("X-AUTH-USER");
        String cacheToken = getCacheToken(headUserAccount);
        // 用户已注销登录
        if (StringUtils.isEmpty(cacheToken)) {
            AuthenticationException authException = new AuthenticationException(USER_LOGOUT);
            response.setStatus(HttpStatus.SC_UNAUTHORIZED);
            response.getOutputStream().write(JSON.toJSONString(authException).getBytes(StandardCharsets.UTF_8));
            return false;
        }

        // 无效token
        if(!TokenUtils.isValidToken(cacheToken)){
            AuthenticationException authException = new AuthenticationException(INVALID_TOKEN);
            response.setStatus(HttpStatus.SC_UNAUTHORIZED);
            response.getOutputStream().write(JSON.toJSONString(authException).getBytes(StandardCharsets.UTF_8));
            return false;
        }

        // 过期token
        if (TokenUtils.isTokenExpired(cacheToken)) {
            AuthenticationException authException = new AuthenticationException(EXPIRED_TOKEN);
            response.setStatus(HttpStatus.SC_UNAUTHORIZED);
            response.getOutputStream().write(JSON.toJSONString(authException).getBytes(StandardCharsets.UTF_8));
            return false;
        }
        return true;
    }
}
