package com.ddpzp.goodbook.util;

import java.lang.reflect.Field;

public class ObjectUtil {

    /**
     * 判断对象的所有属性是否都为空
     *
     * @param object 待判断的对象
     * @return 如果所有属性都为空，返回 true，否则返回 false。
     */
    public static boolean isAllFieldEmpty(Object object) {
        if (object == null) {
            return true;
        }

        // 获取对象的类
        Class<?> clazz = object.getClass();

        // 遍历所有属性，判断是否为空
        for (Field field : clazz.getDeclaredFields()) {
            // 设置访问权限为可访问
            field.setAccessible(true);
            try {
                // 判断属性是否为空
                Object value = field.get(object);
                if (value != null) {
                    if (!"".equals(value) && !"null".equals(value) && !"[]".equals(value)) {
                        return false;  // 只要有一个属性不为空，就返回 false
                    }
                }
            } catch (IllegalAccessException e) {
                // 忽略访问权限异常，继续遍历下一个属性
            }
        }

        return true;
    }
}
