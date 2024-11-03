package com.local.naruto.common.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 参数请求错误异常类
 *
 * @author naruto chen
 * @since 2023-09-25
 */
@Getter
@Setter
public class BadRequestException extends RuntimeException {

    private final int code;
    private final String message;
    private Object data;

    public BadRequestException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
