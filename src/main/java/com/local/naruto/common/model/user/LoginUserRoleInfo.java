package com.local.naruto.common.model.user;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户角色信息
 *
 * @author naruto chen
 * @since 2023-12-16
 */
@Getter
@Setter
public class LoginUserRoleInfo {
    private String userId;
    private String roleId;
    private String roleCode;
    private String roleName;
    private String description;
}
