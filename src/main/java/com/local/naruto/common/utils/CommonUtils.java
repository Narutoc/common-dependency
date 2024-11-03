package com.local.naruto.common.utils;

import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import org.springframework.stereotype.Service;

/**
 * 通用方法工具类
 *
 * @author naruto chen
 * @since 2023-09-27
 */
@Service
public class CommonUtils {

    /**
     * 生成带-的UUID
     *
     * @return UUID
     */
    public static String getGeneratedUuidWithDash() {
        // 生成随机的UUID
        UUID uuid = UUID.randomUUID();
        return new UUID(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits())
            .toString().toUpperCase();
    }

    /**
     * 生成不带带-的UUID
     *
     * @return UUID
     */
    public static String getGeneratedUuidWithoutDash() {
        UUID uuid = UUID.randomUUID();
        return new UUID(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits()).toString()
            .replace("-", "").toUpperCase(Locale.ROOT);
    }

    /**
     * 将对象转为String
     *
     * @param object 对象
     * @return 字符串
     */
    public static String convertObjectToString(Object object) {
        return Objects.isNull(object) ? null : String.valueOf(object);
    }

    /**
     * 四舍五入，保留n位小数
     *
     * @param number 数值
     * @param digital   需要保留小数位数
     * @return 处理后的结果
     */
    public static Double getFixedNumberDigital(Double number, int digital) {
        String formatted = String.format("%." + digital + "f", number);
        return Double.parseDouble(formatted);
    }
}
