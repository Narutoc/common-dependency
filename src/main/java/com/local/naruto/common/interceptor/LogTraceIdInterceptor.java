package com.local.naruto.common.interceptor;

import com.local.naruto.common.constant.Constants;
import com.local.naruto.common.utils.CommonUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 日志traceId拦截器
 *
 * @author naruto chen
 * @since 2023-12-07
 */
public class LogTraceIdInterceptor implements HandlerInterceptor {

    private static final String TRACE_ID = Constants.TRACE_ID;

    /**
     * 日志记录打印，设置traceId，必须为第一个配置
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String traceId = CommonUtils.getGeneratedUuidWithoutDash();
        // 可以考虑传入链路ID，但需保证一定的复杂度唯一性；如果没使用默认UUID自动生成
        if (StringUtils.isNotEmpty(request.getHeader(TRACE_ID))) {
            traceId = request.getHeader(TRACE_ID);
        }
        MDC.put(TRACE_ID, traceId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
        Object handler, @Nullable Exception ex) {
        MDC.remove(TRACE_ID);
    }
}

