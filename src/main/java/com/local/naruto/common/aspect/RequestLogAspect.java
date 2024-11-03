package com.local.naruto.common.aspect;

import com.alibaba.fastjson.JSON;
import com.local.naruto.common.annotations.RequestLog;
import com.local.naruto.common.constant.Constants;
import com.local.naruto.common.exception.ServiceException;
import com.local.naruto.common.utils.CommonUtils;
import java.lang.reflect.Method;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * 接口请求日志自定义注解类
 *
 * @author naruto chen
 * @since 2023-12-09
 */
@Component
@Aspect
@Slf4j
public class RequestLogAspect {

    /**
     * 换行符
     */
    private static final String LINE_SEPARATOR = System.lineSeparator();

    /**
     * 以自定义 @RequestLog 注解为切点
     */
    @Pointcut("@annotation(com.local.naruto.common.annotations.RequestLog)")
    public void RequestLogAspect() {
    }

    /**
     * 在切点之前织入
     *
     * @param joinPoint 切点
     * @throws Throwable 异常
     */
    @Before("RequestLogAspect()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 开始打印请求日志
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();

        // 获取 @RequestLog 注解的描述信息
        String methodDescription = getAspectLogDescription(joinPoint);

        String traceId = CommonUtils.getGeneratedUuidWithoutDash();
        MDC.put(Constants.TRACE_ID, traceId);
        // 打印请求相关参数
        log.info("=================================== Start ====================================");
        // 打印请求 url
        log.info("URL is          : {}", request.getRequestURL().toString());
        // 打印描述信息
        log.info("Description is  : {}", methodDescription);
        // 打印 Http method
        log.info("HTTP Method is  : {}", request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        log.info("Class Method is : {}.{}", joinPoint.getSignature().getDeclaringTypeName(),
            joinPoint.getSignature().getName());
        // 打印请求的 IP
        log.info("IP is           : {}", request.getRemoteAddr());
        // 打印请求入参
        log.info("Request Args are : {}", JSON.toJSONString(joinPoint.getArgs()));
    }

    /**
     * 在切点之后织入
     *
     * @throws Throwable 异常
     */
    @After("RequestLogAspect()")
    public void doAfter() throws Throwable {
        // 接口结束后换行，方便分割查看
        log.info("============================= End ===========================" + LINE_SEPARATOR);
    }

    /**
     * 环绕
     *
     * @param proceedingJoinPoint 处理对象
     * @return 对象
     * @throws Throwable 异常
     */
    @Around("RequestLogAspect()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = null;
        try {
            result = proceedingJoinPoint.proceed();
        } catch (Exception exception) {
            log.info("exception is   :{}", exception.getMessage());
            throw new ServiceException(Constants.INT_500, exception.getMessage());
        } finally {
            // 打印出参
            log.info("Response Args are: {}", JSON.toJSONString(result));
            // 执行耗时
            log.info("Time-Consuming is : {} ms", System.currentTimeMillis() - startTime);
            MDC.clear();
        }
        return result;
    }

    /**
     * 获取切面注解的描述
     *
     * @param joinPoint 切点
     * @return 描述信息
     * @throws Exception 异常
     */
    private String getAspectLogDescription(JoinPoint joinPoint) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class<?> targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        StringBuilder description = new StringBuilder();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    description.append(method.getAnnotation(RequestLog.class).description());
                    break;
                }
            }
        }
        return description.toString();
    }
}
