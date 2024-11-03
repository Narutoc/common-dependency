package com.local.naruto.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * 操作记录日志模型
 *
 * @author naruto chen
 * @since 2023-12-11
 */
@Getter
@Setter
public class OperationLogModel implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private Integer moduleTypeId;
    private String moduleTypeName;
    private Integer functionTypeId;
    private String functionTypeName;
    private String methodName;
    private String requestMethod;
    private String requestUrl;
    private String requestParam;
    private String businessId;
    private String description;
    private Integer operationTypeId;
    private String operationTypeName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;
    private String creator;
}
