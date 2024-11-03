package com.local.naruto.common.annotations;

import com.local.naruto.common.constant.FunctionType;
import com.local.naruto.common.constant.LogModuleType;
import com.local.naruto.common.constant.OperationType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 记录操作日志
 * <a href="https://blog.csdn.net/qq_45525848/article/details/134224450">记录日志博客</a>
 *
 * @author naruto chen
 * @since 2023-12-09
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OperationLog {

    /**
     * 模块类型
     */
    LogModuleType moduleType() default LogModuleType.DEFAULT;

    /**
     * 功能类型
     */
    FunctionType functionType() default FunctionType.DEFAULT;

    /**
     * 业务ID,通常为接口唯一标识位，支持EL表达式
     */
    String businessId() default "";

    /**
     * 业务描述，比如：新增菜单信息
     */
    String description() default "";

    /**
     * 操作类型
     */
    OperationType operationType() default OperationType.QUERY;
}
