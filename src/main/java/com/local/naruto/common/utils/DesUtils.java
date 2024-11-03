package com.local.naruto.common.utils;

import com.local.naruto.common.constant.Constants;
import com.local.naruto.common.exception.BadRequestException;
import com.local.naruto.common.exception.ServiceException;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import org.apache.commons.lang3.StringUtils;

/**
 * DES加密工具类
 *
 * @author naruto chen
 * @since 2023-11-25
 */
public class DesUtils {

    /**
     * 默认编码
     */
    private static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * DES密钥算法
     */
    private static final String DES_ALGORITHM = "DES";

    /**
     * 加密/解密算法-工作模式-填充模式
     */
    private static final String CIPHER_ALGORITHM = "DES/CBC/PKCS5Padding";

    /**
     * 偏移变量，固定占8位字节
     */
    private final static String IV_PARAMETER = "12345678";

    /**
     * DES加密字符串
     *
     * @param password 加密密码，长度不能够小于8位
     * @param data     待加密字符串
     * @return 加密后内容
     */
    public static String desEncrypt(String password, String data) {
        if (StringUtils.isEmpty(password)) {
            throw new BadRequestException(Constants.INT_400, "加密密码为空，无法加密");
        }
        if (password.length() < 8) {
            throw new BadRequestException(Constants.INT_400, "加密密码长度小于8位，无法加密");
        }
        if (StringUtils.isEmpty(data)) {
            return null;
        }
        try {
            Key secretKey = generateKey(password);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes(DEFAULT_CHARSET));
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byte[] bytes = cipher.doFinal(data.getBytes(DEFAULT_CHARSET));
            return Base64LocalUtils.encode(bytes);
        } catch (Exception exception) {
            throw new ServiceException(Constants.INT_500, exception.getMessage());
        }
    }

    /**
     * DES解密字符串
     *
     * @param password 解密密码，长度不能够小于8位
     * @param data     待解密字符串
     * @return 解密后内容
     */
    public static String desDecrypt(String password, String data) {
        if (StringUtils.isEmpty(password)) {
            throw new BadRequestException(Constants.INT_400, "加密密码为空，无法解密");
        }
        if (password.length() < 8) {
            throw new BadRequestException(Constants.INT_400, "加密密码长度小于8位，无法解密");
        }
        if (StringUtils.isEmpty(data)) {
            return null;
        }
        try {
            Key secretKey = generateKey(password);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes(DEFAULT_CHARSET));
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            return new String(cipher.doFinal(Base64LocalUtils.decode(data)), DEFAULT_CHARSET);
        } catch (Exception exception) {
            throw new ServiceException(Constants.INT_500, exception.getMessage());
        }
    }

    /**
     * 生成password对应的key
     *
     * @param password password
     * @return Key
     * @throws Exception 异常
     */
    private static Key generateKey(String password) throws Exception {
        DESKeySpec dks = new DESKeySpec(password.getBytes(DEFAULT_CHARSET));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES_ALGORITHM);
        return keyFactory.generateSecret(dks);
    }
}
