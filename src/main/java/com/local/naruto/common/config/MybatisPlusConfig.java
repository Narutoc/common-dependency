package com.local.naruto.common.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MybatisPlus配置类
 *
 * @author naruto chen
 * @since 2023-12-02
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * 使用多个功能需要注意顺序关系,建议使用如下顺序 多租户,动态表名 分页,乐观锁 sql性能规范, 防止全表更新与删除 总结:
     * 对sql进行单次改造的优先放入,不对sql进行改造的最后放入
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页插件
        interceptor.addInnerInterceptor(paginationInnerInterceptor());
        // 乐观锁插件
        interceptor.addInnerInterceptor(optimisticLockerInnerInterceptor());
        // 阻断插件
        interceptor.addInnerInterceptor(blockAttackInnerInterceptor());
        return interceptor;
    }

    /**
     * 分页插件，自动识别数据库类型 <a href="https://baomidou.com/guide/interceptor-pagination.html">...</a>
     */
    private PaginationInnerInterceptor paginationInnerInterceptor() {
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        paginationInnerInterceptor.setMaxLimit(500L);
        return paginationInnerInterceptor;
    }

    /**
     * 乐观锁插件 <a href="https://baomidou.com/guide/interceptor-optimistic-locker.html">...</a>
     */
    private OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor() {
        return new OptimisticLockerInnerInterceptor();
    }

    /**
     * 元对象字段填充控制器 <a href="https://baomidou.com/guide/auto-fill-metainfo.html">...</a>
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new InitMetaObjectHandler();
    }

    /**
     * 如果是对全表的删除或更新操作，就会终止该操作 <a
     * href="https://baomidou.com/guide/interceptor-block-attack.html">...</a>
     */
    private BlockAttackInnerInterceptor blockAttackInnerInterceptor() {
        return new BlockAttackInnerInterceptor();
    }
}

