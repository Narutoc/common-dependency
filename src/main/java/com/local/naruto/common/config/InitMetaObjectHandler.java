package com.local.naruto.common.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import java.util.Date;
import java.util.Objects;
import com.local.naruto.common.model.user.LoginUserInfo;
import com.local.naruto.common.utils.CurrentUserInfoUtils;
import org.apache.ibatis.reflection.MetaObject;

/**
 * 信息自动注入
 *
 * @author naruto chen
 * @since 2023-12-02
 */
public class InitMetaObjectHandler implements MetaObjectHandler {

    private static final String CREATE_DATE = "createDate";

    private static final String CREATOR = "creator";

    private static final String LAST_MODIFIER = "lastModifier";

    private static final String LAST_MODIFIED_DATE = "lastModifyDate";


    @Override
    public void insertFill(MetaObject metaObject) {
        if (metaObject.hasGetter(CREATE_DATE) && metaObject.getValue(CREATE_DATE) == null) {
            this.setFieldValByName(CREATE_DATE, new Date(), metaObject);
        }
        LoginUserInfo currentUser = CurrentUserInfoUtils.getCurrentUser();
        if (Objects.nonNull(currentUser)) {
            if (metaObject.hasGetter(CREATOR) && metaObject.getValue(CREATOR) == null) {
                this.setFieldValByName(CREATOR, currentUser.getAccount(), metaObject);
            }
            return;
        }
        if (metaObject.hasGetter(CREATOR) && metaObject.getValue(CREATOR) == null) {
            this.setFieldValByName(CREATOR, "naruto-default", metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (metaObject.hasGetter(LAST_MODIFIED_DATE)) {
            this.setFieldValByName(LAST_MODIFIED_DATE, new Date(), metaObject);
        }
        LoginUserInfo currentUser = CurrentUserInfoUtils.getCurrentUser();
        if (Objects.nonNull(currentUser)) {
            if (metaObject.hasGetter(LAST_MODIFIER)) {
                this.setFieldValByName(LAST_MODIFIER, currentUser.getAccount(), metaObject);
            }
            return;
        }
        if (metaObject.hasGetter(LAST_MODIFIER) && metaObject.getValue(LAST_MODIFIER) == null) {
            this.setFieldValByName(LAST_MODIFIER, "naruto-default", metaObject);
        }
    }
}
