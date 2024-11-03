package com.local.naruto.common.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 接口返回通用实体类
 *
 * @author naruto chen
 * @since 2023-09-26
 */
@Getter
@Setter
public class CommonReturnModel {

    @Schema(description = "接口返回码", example = "返回码")
    private int code;
    @Schema(description = "返回信息", example = "OK")
    private String message;
    @Schema(description = "返回数据", example = "[\"Java\",\"Sql\"]")
    private Object data;
}
