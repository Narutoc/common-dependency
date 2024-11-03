package com.local.naruto.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.local.naruto.common.model.user.LoginUserInfo;
import com.local.naruto.common.utils.CurrentUserInfoUtils;
import com.local.naruto.common.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 用户登录信息拦截器
 *
 * @author naruto chen
 * @since 2023-12-18
 */
@Slf4j
public class UserLoginInterceptor implements HandlerInterceptor {

    /**
     * 接口请求头中传入X-AUTH-USER，用户账号，即可从缓存中查询用户信息
     * <p>
     * 用于本地调用接口使用X-AUTH-USER传入的账号信息
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userAccount = request.getHeader("X-AUTH-USER");
        log.info("X-AUTH-USER is {}", userAccount);
        RedissonClient redisBean = SpringContextUtils.getBean(RedissonClient.class);
        if (StringUtils.isNotEmpty(userAccount) && Objects.nonNull(redisBean)) {
            RBucket<Object> userBucket = redisBean.getBucket("user_account_cache_" + userAccount, JsonJacksonCodec.INSTANCE);
            Object userObject = userBucket.get();
            if (Objects.nonNull(userObject)) {
                LoginUserInfo userModel = JSON.parseObject(JSON.toJSONString(userObject), LoginUserInfo.class);
                CurrentUserInfoUtils.setCurrentUser(userModel);
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) {
        CurrentUserInfoUtils.removeCurrentUser();
    }
}
