package com.local.naruto.common.constant;

import lombok.Getter;

/**
 * 操作 类型枚举
 *
 * @author naruto chen
 * @since 2023-09-26
 */
@Getter
public enum OperationType {
    CREATE(1, "create"),
    UPDATE(2, "update"),
    DELETE(3, "delete"),
    REVERT(4, "revert"),
    QUERY(5, "query"),
    ;

    private int index;
    private String description;

    OperationType(int index, String description) {
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
