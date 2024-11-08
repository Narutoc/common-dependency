package com.local.naruto.common.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口请求日志自定义注解
 *
 * @author naruto chen
 * @since 2023-12-09
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface RequestLog {

    /**
     * 日志描述信息
     *
     * @return 描述
     */
    String description() default "";
}
