package com.local.naruto.common.constant;

import lombok.Getter;

/**
 * 通告类型枚举
 *
 * @author naruto chen
 * @since 2023-12-30
 */
@Getter
public enum NoticeType {
    系统公告(1,"系统公告"),
    升级公告(2,"升级公告"),
    福利公告(3,"福利公告"),
    安全公告(4,"安全公告"),
    业务公告(5,"业务公告"),
    活动公告(6,"活动公告"),
    ;

    private int index;
    private String description;

    NoticeType(int index, String description) {
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
