package com.local.naruto.common.constant;

import lombok.Getter;

/**
 * 业务模块类型枚举
 *
 * @author naruto chen
 * @since 2023-12-30
 */
@Getter
public enum BusinessModuleType {
    通用模块(1,"通用模块"),
    活动中心(2,"活动中心"),
    个人资产(3,"个人资产"),
    Cloud_Compute(4,"Cloud Compute"),
    ;

    private int index;
    private String description;

    BusinessModuleType(int index, String description) {
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
