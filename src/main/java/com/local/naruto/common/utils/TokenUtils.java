package com.local.naruto.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.local.naruto.common.constant.Constants;
import com.local.naruto.common.exception.ServiceException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户token工具类
 * <a href="https://www.cnblogs.com/yuanchangliang/p/16373162.html">参考文档</a>
 *
 * @author naruto chen
 * @since 2023-12-18
 */
@Slf4j
public class TokenUtils {

    /**
     * token过期时间，一天
     */
    private static final long EXPIRE_TIME = 60 * 60 * 24 * 1000;

    /**
     * token秘钥
     */
    private static final String TOKEN_SECRET = "naruto@token@secret";

    /**
     * JWT中的过期时间戳
     */
    private static final String EXPIRE_TIME_STRING = "expiredTimeString";

    /**
     * 生成签名，1天过期
     *
     * @param account 用户名
     * @return 生成的token
     */
    public static String generateToken(String account) {
        try {
            // 设置过期时间
            Date expiredDate = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            log.info("expiredDate is {}", expiredDate);
            String expiredTimeString = JSON.toJSONString(expiredDate);
            // 设置头部信息
            Map<String, Object> header = new HashMap<>(2);
            header.put("Type", "Jwt");
            header.put("alg", "HS256");
            // 返回token字符串
            return JWT.create()
                .withHeader(header)
                // 将登录账号信息放在JWT的payload中
                .withClaim("account", account)
                // 过期
                .withClaim(EXPIRE_TIME_STRING, expiredTimeString)
                .withExpiresAt(expiredDate)
                // 私钥和加密算法
                .sign(Algorithm.HMAC256(TOKEN_SECRET));
        } catch (Exception e) {
            throw new ServiceException(Constants.INT_500, e.getMessage());
        }
    }

    /**
     * 检验token是否有效
     *
     * @param token 需要校验的token
     * @return 校验是否成功
     */
    public static boolean isValidToken(String token) {
        try {
            //设置签名的加密算法：HMAC256
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 从token中获取账号
     *
     * @param token 需要校验的token
     * @return 用户账号
     */
    public static String getUserAccountByToken(String token) {
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        // 从JWT的payload中获取登录用户的账号
        String payload = new String(decoder.decode(chunks[1]));
        JSONObject userInform = JSON.parseObject(payload);
        return userInform.get("account").toString();
    }

    /**
     * 校验token是否过期从token中获取账号
     *
     * @param token 需要校验的token
     * @return 是否过期
     */
    public static boolean isTokenExpired(String token) {
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(chunks[1]));
        JSONObject userInform = JSON.parseObject(payload);
        // 过期时间戳
        String expiredTimeString = String.valueOf(userInform.get(EXPIRE_TIME_STRING));
        long expiredLong = Long.parseLong(expiredTimeString);
        Date expiredDate = new Date(expiredLong);
        // 过期时间在当前时间之前，则表示token已过期
        return expiredDate.before(new Date());
    }
}
