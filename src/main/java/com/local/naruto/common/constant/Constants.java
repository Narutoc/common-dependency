package com.local.naruto.common.constant;

import java.net.URI;

/**
 * 常量类
 *
 * @author naruto chen
 * @since 2023-09-26
 */
public class Constants {

    // 字符串0
    public static final String STRING_ZERO = "0";

    // 字符串1
    public static final String STRING_ONE = "1";

    // 字符串2
    public static final String STRING_TWO = "2";

    // 常量0
    public static final int NUM_ZERO = 0;

    // 常量1
    public static final int NUM_ONE = 1;

    // 常量2
    public static final int NUM_TWO = 2;

    // 常量10
    public static final int NUM_TEN = 10;

    // 常量1000
    public static final int NUN_THOUSAND = NUM_TEN * NUM_TEN;

    // 常量200
    public static final int INT_200 = 200;

    // 常量400
    public static final int INT_400 = 400;

    // 常量500
    public static final int INT_500 = 500;

    public static final URI DEFAULT_TYPE = URI.create("badRequest");

    // 常量10086
    public static final int INT_10086 = 10086;

    public static final String TRACE_ID = "TRACE_ID";

    public static final String CHARSET_DEFAULT = "UTF-8";
    public static final int CONNECT_TIMEOUT = 3000;
    public static final int SOCKET_TIMEOUT = 3000;
    public static final String USER_TOKEN = "user_token_";
    // 请求头中的token标识
    public static final String HEADER_TOKEN = "token";
    public static final String FULL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String YMD_DATE_FORMAT = "yyyy-MM-dd";
}
