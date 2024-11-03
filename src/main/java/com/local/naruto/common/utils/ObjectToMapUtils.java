package com.local.naruto.common.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ObjectToMap工具类
 *
 * @author naruto chen
 * @since 2023-12-11
 */
public class ObjectToMapUtils {

    /**
     * 将Object对象里面的属性和值转化成Map对象
     *
     * @param obj 转换对象
     * @return map
     * @throws IllegalAccessException 异常
     */
    public static Map<String, String> objectToMap(Object obj) throws IllegalAccessException {
        if (obj == null) {
            return null;
        }
        Map<String, String> map = new HashMap<>();
        List<Field> allField = getAllField(obj);
        for (Field field : allField) {
            field.setAccessible(true);
            String fieldName = field.getName();
            String fieldValue = "";
            if (field.getType() == String.class || field.getType() == Integer.class
                || field.getType() == int.class) {
                fieldValue = field.get(obj) == null ? "" : field.get(obj).toString();
            }
            map.put(fieldName, fieldValue);
        }
        return map;
    }

    private static List<Field> getAllField(Object obj) {
        List<Field> fieldList = new ArrayList<>();
        Class<?> clazz = obj.getClass();
        while (clazz != null) {
            fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fieldList;

    }
}
