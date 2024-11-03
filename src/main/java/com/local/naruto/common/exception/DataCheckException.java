package com.local.naruto.common.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 数据校验异常类
 *
 * @author naruto chen
 * @since 2023-09-25
 */
@Getter
@Setter
public class DataCheckException extends RuntimeException {

    private final int code;
    private final String message;
    private Object data;

    public DataCheckException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
