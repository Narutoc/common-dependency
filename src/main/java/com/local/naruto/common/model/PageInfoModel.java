package com.local.naruto.common.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.List;

/**
 * 分页查询列表结果通用实体类
 *
 * @author naruto chen
 * @since 2023-09-26
 */
@Getter
@Setter
public class PageInfoModel<T> implements Serializable {

    @Schema(description = "当前页码", example = "1")
    private int current;

    @Schema(description = "每页显示条数", example = "10")
    private int pageSize;

    @Schema(description = "数据总条数", example = "25")
    private long total;

    @Schema(description = "总页数", example = "3")
    private int totalPages;

    @Schema(description = "数据列表", type = "list", example = "[\"Java\",\"Sql\"]")
    private List<T> data;
}
