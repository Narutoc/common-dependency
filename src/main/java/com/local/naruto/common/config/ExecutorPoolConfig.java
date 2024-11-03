package com.local.naruto.common.config;

import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 线程池配置
 *
 * @author naruto chen
 * @since 2023-12-02
 */
@EnableAsync
@Configuration
public class ExecutorPoolConfig {

    @Bean
    public TaskExecutor asyncServiceExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 核心线程数-以8核计算 2*N
        taskExecutor.setCorePoolSize(16);
        taskExecutor.setAllowCoreThreadTimeOut(true);
        // 最大线程数-以8核计算 2*N + 1
        taskExecutor.setMaxPoolSize(17);
        // 配置队列大小 50
        taskExecutor.setQueueCapacity(50);
        // KeepAlive 时间设置为30s
        taskExecutor.setKeepAliveSeconds(30);
        // 配置线程池前缀
        taskExecutor.setThreadNamePrefix("naruto-local-");
        // 将traceId传给子线程
        taskExecutor.setTaskDecorator(new MdcTaskDecorator());
        // 拒绝策略
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.initialize();
        return taskExecutor;
    }
}
