package com.local.naruto.common.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 服务错误异常类
 *
 * @author naruto chen
 * @since 2023-09-25
 */
@Getter
@Setter
public class ServiceException extends RuntimeException {

    private int code;
    private String message;

    /**
     * 带返回码的构造函数
     *
     * @param code    错误码
     * @param message 错误信息
     */
    public ServiceException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
