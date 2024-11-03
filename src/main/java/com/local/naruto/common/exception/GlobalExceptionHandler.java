package com.local.naruto.common.exception;

import com.local.naruto.common.constant.Constants;
import com.local.naruto.common.model.CommonReturnModel;
import com.local.naruto.common.utils.RestResponseUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 全局异常处理
 *
 * @author naruto chen
 * @since 2023-12-06
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理其他异常
     *
     * @param request   请求
     * @param exception 异常
     * @return 错误信息
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonReturnModel exceptionHandler(HttpServletRequest request, Exception exception) {
        LOGGER.error("系统异常信息堆栈：", exception);
        return RestResponseUtils.restfulFail(Constants.INT_500, exception.toString());
    }

    /**
     * BadRequestException异常处理
     *
     * @param request   请求
     * @param exception 异常
     * @return 错误信息
     */
    @ExceptionHandler(value = BadRequestException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonReturnModel badRequestExceptionHandler(HttpServletRequest request, BadRequestException exception) {
        return RestResponseUtils.restfulFail(exception.getCode(), exception.getMessage());
    }

    /**
     * BadRequestException异常处理
     *
     * @param request   请求
     * @param exception 异常
     * @return 错误信息
     */
    @ExceptionHandler(value = DataCheckException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonReturnModel dataCheckExceptionHandler(HttpServletRequest request, DataCheckException exception) {
        return RestResponseUtils.restfulFail(exception.getCode(), exception.getMessage());
    }

    /**
     * ServiceException异常处理
     *
     * @param request   请求
     * @param exception 异常
     * @return 错误信息
     */
    @ExceptionHandler(value = ServiceException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonReturnModel serviceExceptionHandler(HttpServletRequest request, ServiceException exception) {
        LOGGER.error("服务异常信息堆栈：", exception);
        return RestResponseUtils.restfulFail(exception.getCode(), exception.getMessage());
    }
}
