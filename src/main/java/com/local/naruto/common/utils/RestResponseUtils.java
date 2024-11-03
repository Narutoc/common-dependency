package com.local.naruto.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.local.naruto.common.constant.Constants;
import com.local.naruto.common.model.CommonReturnModel;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;


/**
 * 接口返回包装工具类
 *
 * @author naruto chen
 * @since 2023-09-26
 */
public class RestResponseUtils {

    /**
     * 返回成功
     *
     * @param response 请求响应
     * @param data     数据
     */
    public static void servletSuccess(HttpServletResponse response, Object data) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        String restfulString = JSONObject.toJSONString(restfulSuccess(data));
        ServletOutputStream out = response.getOutputStream();
        out.write(restfulString.getBytes(StandardCharsets.UTF_8));
        out.flush();
        out.close();
    }

    /**
     * 返回成功
     *
     * @param data 数据
     * @return 成功返回码和信息
     */
    public static CommonReturnModel restfulSuccess(Object data) {
        CommonReturnModel returnModel = new CommonReturnModel();
        returnModel.setCode(Constants.INT_200);
        returnModel.setData(data);
        return returnModel;
    }

    /**
     * 返回失败
     *
     * @param message 错误信息
     * @return 成功返回码和信息
     */
    public static CommonReturnModel restfulFail(int code, String message) {
        CommonReturnModel returnModel = new CommonReturnModel();
        returnModel.setCode(code);
        returnModel.setMessage(message);
        return returnModel;
    }
}
