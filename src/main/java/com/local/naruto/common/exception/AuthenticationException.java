package com.local.naruto.common.exception;

import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpStatus;

/**
 * 鉴权异常信息
 *
 * @author naruto chen
 * @since 2023-12-23
 */
@Getter
@Setter
public class AuthenticationException {
    private final int code;
    private final String message;
    private Object data;

    public AuthenticationException(String message) {
        this.code = HttpStatus.SC_UNAUTHORIZED;
        this.message = message;
    }
}
