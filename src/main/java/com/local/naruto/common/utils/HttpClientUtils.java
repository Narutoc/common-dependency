package com.local.naruto.common.utils;

import com.local.naruto.common.constant.Constants;
import com.local.naruto.common.exception.ServiceException;
import com.local.naruto.common.interceptor.HttpClientTraceIdInterceptor;
import com.local.naruto.common.model.HttpResultModel;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

/**
 * Httpclient工具类
 *
 * @author naruto chen
 * @since 2023-12-11
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HttpClientUtils {

    private final static String CHARSET_DEFAULT = "UTF-8";
    private static final int CONNECT_TIMEOUT = 3000;
    private static final int SOCKET_TIMEOUT = 3000;

    private final CloseableHttpClient httpClient = HttpClientBuilder.create()
        .addInterceptorFirst(new HttpClientTraceIdInterceptor())
        .build();

    /**
     * GET请求，返回string
     *
     * @param url 请求地址
     * @return string
     */
    public String doGetRequestResultString(String url) {
        try {
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            return EntityUtils.toString(response.getEntity());
        } catch (IOException exception) {
            log.error("doGetRequest error is : {}", exception.getMessage());
            throw new ServiceException(Constants.INT_500, exception.getMessage());
        }
    }

    /**
     * GET请求
     *
     * @param url 请求地址
     * @return string
     */
    public HttpResultModel doGetRequestResultModel(String url) throws IOException {
        CloseableHttpResponse resp = null;
        HttpResultModel result = new HttpResultModel();
        try {
            HttpGet httpGet = new HttpGet(url);

            RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT).build();
            httpGet.setConfig(requestConfig);

            resp = httpClient.execute(httpGet);
            String body = EntityUtils.toString(resp.getEntity(), CHARSET_DEFAULT);
            int statusCode = resp.getStatusLine().getStatusCode();
            result.setStatus(statusCode);
            result.setBody(body);
        } finally {
            if (null != resp) {
                resp.close();
            }
        }
        return result;
    }

    /**
     * post请求  编码格式默认UTF-8
     *
     * @param url           请求url
     * @param requestObject 请求对象
     * @return http返回体
     */
    public HttpResultModel doPostRequest(String url, Object requestObject) throws IOException {
        CloseableHttpResponse closeableHttpResponse = null;
        try {
            HttpPost httpPost = getHttpPost(url, requestObject);
            closeableHttpResponse = httpClient.execute(httpPost);
            String body = EntityUtils.toString(closeableHttpResponse.getEntity(),
                Constants.CHARSET_DEFAULT);
            int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
            HttpResultModel result = new HttpResultModel();
            result.setStatus(statusCode);
            result.setBody(body);
            return result;
        } catch (IOException | IllegalAccessException exception) {
            log.error("doPostRequest error is : {}", exception.getMessage());
            throw new ServiceException(Constants.INT_500, exception.getMessage());
        } finally {
            if (null != closeableHttpResponse) {
                closeableHttpResponse.close();
            }
        }
    }

    private HttpPost getHttpPost(String url, Object requestObject)
        throws UnsupportedEncodingException, IllegalAccessException {
        Map<String, String> paramsMap = ObjectToMapUtils.objectToMap(requestObject);
        HttpPost httpPost = new HttpPost(url);

        RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(Constants.CONNECT_TIMEOUT)
            .setSocketTimeout(Constants.SOCKET_TIMEOUT).build();
        httpPost.setConfig(requestConfig);

        if (paramsMap != null && !paramsMap.isEmpty()) {
            List<NameValuePair> pairList = new ArrayList<>();
            for (String fieldName : paramsMap.keySet()) {
                String fieldValue = paramsMap.get(fieldName);
                BasicNameValuePair basicNameValuePair = new BasicNameValuePair(fieldName,
                    fieldValue);
                pairList.add(basicNameValuePair);
            }
            httpPost.setEntity(new UrlEncodedFormEntity(pairList, Constants.CHARSET_DEFAULT));
        }
        return httpPost;
    }
}
