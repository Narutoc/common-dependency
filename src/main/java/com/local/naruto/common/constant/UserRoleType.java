package com.local.naruto.common.constant;

import lombok.Getter;

/**
 * 用户角色 类型枚举
 *
 * @author naruto chen
 * @since 2023-12-31
 */
@Getter
public enum UserRoleType {

    超级管理员(1, "超级管理员"),
    普通管理员(2, "普通管理员"),
    区域总经理(3, "区域总经理"),
    业务经理(4, "业务经理"),
    技术主管(5, "技术主管"),
    后端开发人员(6, "后端开发人员"),
    前端开发人员(7, "前端开发人员"),
    运维人员(8, "运维人员"),
    销售人员(9, "销售人员"),
    技术支持(10, "技术支持"),
    DevOps(11, "DevOps"),
    DBA(11, "数据库管理员"),
    ;

    private int index;
    private String role;

    UserRoleType(int index, String role) {
        this.index = index;
        this.role = role;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setRole(String description) {
        this.role = description;
    }

}
