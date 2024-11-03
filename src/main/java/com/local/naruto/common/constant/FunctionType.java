package com.local.naruto.common.constant;

import lombok.Getter;

/**
 * 日志操作功能枚举
 *
 * @author naruto chen
 * @since 2023-12-09
 */
@Getter
public enum FunctionType {
    DEFAULT(1, "默认功能"),
    ;

    private int index;
    private String description;

    FunctionType(int index, String description) {
        this.index = index;
        this.description = description;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
