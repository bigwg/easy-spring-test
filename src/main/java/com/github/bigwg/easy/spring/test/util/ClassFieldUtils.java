package com.github.bigwg.easy.spring.test.util;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 类字段操作工具
 *
 * @author zhaozhiwei
 * @since 2024/3/3
 */
public class ClassFieldUtils {

    /**
     * 获取目标类自身以及所有父类的所有字段
     *
     * @param clazz
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static List<Field> getSelfAndSuperFields(Class<?> clazz) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<Field> fields = new LinkedList<>();
        fields.addAll(getFields(clazz));
        Class<?> superClass = clazz.getSuperclass();
        while (superClass != null) {
            fields.addAll(getFields(superClass));
            superClass = superClass.getSuperclass();
        }
        return fields;
    }

    /**
     * 获取指定类所有字段
     *
     * @param clazz
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static List<Field> getFields(Class<?> clazz) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getDeclaredFields = ReflectionUtils.class.getDeclaredMethod("getDeclaredFields", Class.class);
        getDeclaredFields.setAccessible(true);
        return Arrays.asList((Field[]) getDeclaredFields.invoke(ReflectionUtils.class, clazz));
    }

}
