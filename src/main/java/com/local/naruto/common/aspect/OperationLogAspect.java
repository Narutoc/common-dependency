package com.local.naruto.common.aspect;

import com.alibaba.fastjson.JSON;
import com.local.naruto.common.annotations.OperationLog;
import com.local.naruto.common.constant.Constants;
import com.local.naruto.common.exception.ServiceException;
import com.local.naruto.common.model.OperationLogModel;
import com.local.naruto.common.utils.CommonUtils;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Objects;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * 记录操作日志的AOP
 *
 * @author naruto chen
 * @since 2023-12-09
 */
@Component
@Aspect
@Slf4j
public class OperationLogAspect {

    /**
     * 设置操作日志切入点 记录操作日志 在注解的位置切入代码
     */
    @Pointcut("@annotation(com.local.naruto.common.annotations.OperationLog)")
    public void operationLogPointcut() {

    }

    /**
     * 设置操作异常切入点记录异常日志 扫描所有controller包下操作
     */
    @Pointcut("execution(* com.local.naruto.*.controller..*.*(..))")
    public void operationExceptionLogPointcut() {

    }

    /**
     * 正常返回通知，拦截用户操作日志，连接点正常执行完成后执行， 如果连接点抛出异常，则不会执行
     *
     * @param joinPoint 切入点
     * @param result    返回结果
     */
    @AfterReturning(value = "operationLogPointcut()", returning = "result")
    public void saveOperationLog(JoinPoint joinPoint, Object result) {
        try {
            log.info("start to save operation log");
            OperationLogModel logModel = getOperationLogModel(joinPoint);
            log.info("operationLogModel is {}", JSON.toJSONString(logModel));
            // TODO 操作日志入库待实现
            log.info("save operation log successfully");
        } catch (Exception exception) {
            throw new ServiceException(Constants.INT_500, exception.getMessage());
        }
    }

    /**
     * 异常返回通知，用于拦截异常日志信息 连接点抛出异常后执行
     *
     * @param joinPoint 切入点
     * @param exception 异常信息
     */
    @AfterThrowing(pointcut = "operationExceptionLogPointcut()", throwing = "exception")
    public void saveExceptionLog(JoinPoint joinPoint, Throwable exception) {
        try {
            log.info("start to save exception operation log");
            // TODO 异常操作日志入库
            log.info("save exception operation log successfully");
        } catch (Exception ex) {
            throw new ServiceException(Constants.INT_500, exception.getMessage());
        }
    }

    private OperationLogModel getOperationLogModel(JoinPoint joinPoint) {
        OperationLogModel logModel = new OperationLogModel();
        logModel.setId(CommonUtils.getGeneratedUuidWithoutDash());
        logModel.setCreator("Naruto");
        logModel.setCreateDate(new Date());
        // 从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取切入点所在的方法
        Method method = signature.getMethod();
        setLogByAnnotation(method, logModel);
        setLogByRequest(method, logModel, joinPoint);
        OperationLog operationLog = method.getAnnotation(OperationLog.class);
        String businessId = getBusinessId(joinPoint.getArgs(), operationLog.businessId(), method);
        logModel.setBusinessId(businessId);
        return logModel;
    }

    /**
     * 将注解传入的信息入库
     */
    private void setLogByAnnotation(Method method, OperationLogModel logModel) {
        OperationLog operationLog = method.getAnnotation(OperationLog.class);
        if (Objects.nonNull(operationLog)) {
            logModel.setModuleTypeId(operationLog.moduleType().getIndex());
            logModel.setModuleTypeName(operationLog.moduleType().getDescription());
            logModel.setOperationTypeName(operationLog.moduleType().getDescription());
            logModel.setFunctionTypeId(operationLog.functionType().getIndex());
            logModel.setFunctionTypeName(operationLog.functionType().getDescription());
            logModel.setBusinessId(operationLog.businessId());
            logModel.setDescription(operationLog.description());
            logModel.setOperationTypeId(operationLog.operationType().getIndex());
            logModel.setOperationTypeName(operationLog.operationType().getDescription());
        }
    }

    /**
     * 将请求的信息入库
     */
    private void setLogByRequest(Method method, OperationLogModel logModel, JoinPoint joinPoint) {
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest)
            requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        assert request != null;
        logModel.setRequestUrl(request.getRequestURI());
        logModel.setRequestMethod(request.getMethod());
        // 将入参转换成json
        String params = parseArgsArrayToString(joinPoint.getArgs());
        logModel.setRequestParam(params);
        // 获取请求的类名
        String className = joinPoint.getTarget().getClass().getName();
        // 获取请求的方法名
        String methodName = method.getName();
        methodName = className + "." + methodName + "()";
        logModel.setMethodName(methodName);
    }

    /**
     * 获取业务ID
     *
     * @param jointArgs 切点参数
     * @param inputId   业务ID名称 #id
     * @return 业务ID
     */
    private String getBusinessId(Object[] jointArgs, String inputId, Method method) {
        StandardReflectionParameterNameDiscoverer discoverer = new StandardReflectionParameterNameDiscoverer();
        String[] params = discoverer.getParameterNames(method);
        EvaluationContext context = new StandardEvaluationContext();
        for (int length = 0; length < Objects.requireNonNull(params).length; length++) {
            context.setVariable(params[length], jointArgs[length]);
        }
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(inputId);
        Object businessId = expression.getValue(context);
        return CommonUtils.convertObjectToString(businessId);
    }

    /**
     * 将参数组成的数组组成字符串
     */
    private String parseArgsArrayToString(Object[] paramsArray) {
        StringBuilder paramBuilder = new StringBuilder();
        if (null == paramsArray) {
            return paramBuilder.toString().trim();
        }
        for (Object element : paramsArray) {
            if (null != element) {
                try {
                    Object elementJsonObject = JSON.toJSON(element);
                    paramBuilder.append(elementJsonObject.toString()).append(" ");
                } catch (Exception exception) {
                    log.error("argsArrayToString error is {}", exception.getMessage());
                    throw new ServiceException(Constants.INT_500, exception.getMessage());
                }
            }
        }
        return paramBuilder.toString().trim();
    }

}
