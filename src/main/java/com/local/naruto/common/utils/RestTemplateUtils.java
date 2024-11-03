package com.local.naruto.common.utils;

import com.local.naruto.common.interceptor.RestTemplateTraceIdInterceptor;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate工具类
 *
 * @author naruto chen
 * @since 2023-09-26
 */
@Component
@RequiredArgsConstructor
public class RestTemplateUtils {

    private final RestTemplate restTemplate;

    /**
     * GET请求
     *
     * @param url 请求地址
     * @return string
     */
    public String doGetRequest(String url) {
        restTemplate.setInterceptors(List.of(new RestTemplateTraceIdInterceptor()));
        return restTemplate.getForObject(url, String.class);
    }

    /**
     * POST请求
     *
     * @param url 请求地址
     * @return string
     */
    public String doPostRequest(String url, Object object) {
        restTemplate.setInterceptors(List.of(new RestTemplateTraceIdInterceptor()));
        return restTemplate.postForObject(url, object, String.class);
    }

}
