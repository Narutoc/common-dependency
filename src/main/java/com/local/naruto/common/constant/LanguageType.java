package com.local.naruto.common.constant;

import lombok.Getter;

/**
 * 语言 类型枚举
 *
 * @author naruto chen
 * @since 2023-09-26
 */
@Getter
public enum LanguageType {
    ZH_CN(1, "zh-CN"),
    ZH_HK(2, "zh_HK"),
    ZH_TW(3, "zh-TW"),
    EN_GB(4, "en-GB"),
    EN_US(5, "en-US");

    private int index;
    private String description;

    LanguageType(int index, String description) {
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
