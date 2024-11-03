package com.local.naruto.common.utils;

import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import java.util.Objects;

/**
 * spring环境信息上下文工具类
 *
 * @author naruto chen
 * @since 2023-12-09
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {

    /**
     * Spring应用上下文环境
     */
    private static ApplicationContext applicationContext;

    /**
     * 获取name对应的bean对象
     *
     * @param name  Bean的名称
     * @param clazz 类型
     * @param <T>   泛型定义
     * @return 对象
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return Objects.isNull(applicationContext) ? null : applicationContext.getBean(name, clazz);
    }

    public static <T> T getBean(Class<T> clazz) {
        return Objects.isNull(applicationContext) ? null : applicationContext.getBean(clazz);
    }

    /**
     * 获取name对应的Bean实例
     *
     * @param name Bean名称
     * @return 对象
     */
    public static Object getBean(String name) {
        return Objects.isNull(applicationContext) ? null :applicationContext.getBean(name);
    }

    /**
     * 设置上下文
     *
     * @param applicationContext 上下文
     * @throws BeansException 异常
     */
    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext)
        throws BeansException {
        SpringContextUtils.applicationContext = applicationContext;
    }

    /**
     * 获取Spring应用上下文
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取Environment
     *
     * @return 环境实体
     */
    public static Environment getEnvironment() {
        return applicationContext.getEnvironment();
    }
}
