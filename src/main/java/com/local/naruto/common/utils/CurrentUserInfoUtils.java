package com.local.naruto.common.utils;

import com.local.naruto.common.model.user.LoginUserInfo;

/**
 * 当前登录用户信息工具类
 *
 * @author naruto chen
 * @since 2023-12-19
 */
public class CurrentUserInfoUtils {

    private final static ThreadLocal<LoginUserInfo> USER = new ThreadLocal<>();

    /**
     * 将当前用户信息放入ThreadLocal
     */
    public static void setCurrentUser(LoginUserInfo currentUser) {
        USER.set(currentUser);
    }

    /**
     * 获取当前ThreadLocal的用户信息
     */
    public static LoginUserInfo getCurrentUser() {
        return USER.get();
    }

    /**
     * 删除ThreadLocal中的数据
     */
    public static void removeCurrentUser() {
        USER.remove();
    }
}
