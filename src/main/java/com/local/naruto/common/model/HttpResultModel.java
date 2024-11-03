package com.local.naruto.common.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Http返回结果
 *
 * @author naruto chen
 * @since 2023-12-11
 */
@Getter
@Setter
public class HttpResultModel {

    /**
     * HTTP状态码
     */
    private int status;
    /**
     * HTTP响应正文
     */
    private String body;
    /**
     * 错误信息
     */
    private String error;

    public HttpResultModel() {
    }

    public HttpResultModel(int status, String body, String error) {
        this.status = status;
        this.body = body;
        this.error = error;
    }
}
