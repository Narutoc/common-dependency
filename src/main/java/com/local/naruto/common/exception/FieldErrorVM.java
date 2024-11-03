package com.local.naruto.common.exception;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * 虚拟机异常类
 *
 * @author naruto chen
 * @since 2023-09-25
 */
@Getter
@Setter
public class FieldErrorVM implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String objectName;
    private final String field;
    private final String message;

    public FieldErrorVM(String objectName, String field, String message) {
        this.objectName = objectName;
        this.field = field;
        this.message = message;
    }
}
