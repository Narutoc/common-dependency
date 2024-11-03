package com.local.naruto.common.model.user;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

/**
 * 用户登录信息实体
 *
 * @author naruto chen
 * @since 2023-12-19
 */
@Getter
@Setter
public class LoginUserInfo {
    private String userId;
    private String userName;
    private String account;
    private String telephone;
    private String mobile;
    private String email;
    private List<LoginUserMenuInfo> menuList;
    private List<LoginUserRoleInfo> roleList;
}
