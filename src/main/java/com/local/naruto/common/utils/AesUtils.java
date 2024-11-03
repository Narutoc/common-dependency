package com.local.naruto.common.utils;

import com.local.naruto.common.constant.Constants;
import com.local.naruto.common.exception.ServiceException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AES加密工具类
 *
 * @author naruto chen
 * @since 2023-09-25
 */
public class AesUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(AesUtils.class);

    private static final String CHAR_SET = "utf-8";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
    private static final String ALGORITHM = "AES";

    /**
     * 对content加密
     *
     * @param content 需要加密的内容
     * @param key     加密密码
     * @return 加密结果
     */
    public static String encrypt(String content, String key) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            byte[] byteContent = content.getBytes(CHAR_SET);
            // 初始化
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] result = cipher.doFinal(byteContent);
            // 加密
            return Base64LocalUtils.encode(result);
        } catch (Exception exception) {
            LOGGER.info("encrypt error is :{}", exception.getMessage());
            throw new ServiceException(Constants.INT_500, exception.getMessage());
        }
    }

    /**
     * AES（256）解密
     *
     * @param content 待解密内容
     * @param key     解密密钥
     * @return 解密之后
     */
    public static String decrypt(String content, String key) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            // 初始化
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] result = cipher.doFinal(Base64LocalUtils.decode(content));
            // 解密
            return new String(result);
        } catch (Exception exception) {
            LOGGER.info("decrypt exception is {}:", exception.getMessage());
            throw new ServiceException(Constants.INT_500, exception.getMessage());
        }
    }

    /**
     * 生产秘钥key
     *
     * @param password password
     * @return SecretKey
     */
    public static SecretKey generatorKey(String password) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
            keyGenerator.init(128, new SecureRandom(password.getBytes()));
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException exception) {
            LOGGER.info("generatorKey exception is : {}", exception.getMessage());
            throw new ServiceException(Constants.INT_500, exception.getMessage());
        }
    }
}