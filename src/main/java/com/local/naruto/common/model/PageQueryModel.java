package com.local.naruto.common.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.util.Map;

/**
 * 分页查询条件通用实体类
 *
 * @author naruto chen
 * @since 2023-09-26
 */
@Getter
@Setter
public class PageQueryModel<T> {

    @Schema(description = "每页显示数量", example = "10")
    private int pageSize;
    @Schema(description = "当前页码", example = "1")
    private int current;
    private Map<String, Object[]> filter;
    private Map<String, Object> sorter;
    @Schema(description = "查询条件", example = "模拟查询条件")
    private T queryModel;
}
