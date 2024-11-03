package com.local.naruto.common.interceptor;

import com.local.naruto.common.constant.Constants;
import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;
import org.slf4j.MDC;

/**
 * HttpClient请求的traceId
 *
 * @author naruto chen
 * @since 2023-12-10
 */
public class HttpClientTraceIdInterceptor implements HttpRequestInterceptor {

    /**
     * httpClient请求加入traceId
     */
    @Override
    public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
        String traceId = MDC.get(Constants.TRACE_ID);
        //当前线程调用中有traceId，则将该traceId进行透传
        if (traceId != null) {
            //添加请求体
            httpRequest.addHeader(Constants.TRACE_ID, traceId);
        }
    }
}