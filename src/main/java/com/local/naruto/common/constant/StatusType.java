package com.local.naruto.common.constant;

import lombok.Getter;

/**
 * 状态 类型枚举
 *
 * @author naruto chen
 * @since 2023-09-26
 */
@Getter
public enum StatusType {
    IN_USE(1, "启用"),
    NOT_USE(2, "停用"),
    DELETE(3, "删除"),
    DRAFT(4, "草稿"),
    已提交待审核(5, "已提交待审核"),
    审核已通过(6, "审核已通过"),
    审核未通过(7, "审核未通过"),
    未同步(8, "未同步"),
    同步中(9, "同步中"),
    同步成功(10, "同步成功"),
    同步失败(11, "同步失败"),
    已确认(12, "已确认"),
    未确认(13, "未确认"),
    ;

    private int code;
    private String status;

    StatusType(int code, String status) {
        this.code = code;
        this.status = status;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
