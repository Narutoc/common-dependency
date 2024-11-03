package com.local.naruto.common.model.user;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

/**
 * 用户菜单信息
 *
 * @author naruto chen
 * @since 2023-12-19
 */
@Getter
@Setter
public class LoginUserMenuInfo {
    private String userId;
    private String menuId;
    private String menuName;
    private String accessUrl;
    private String isHotMenu;
    private List<LoginUserMenuInfo> childMenu;
}
