package com.github.bigwg.easy.spring.test.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * SuperClassUtils
 *
 * @author zhaozhiwei
 * @since 2024/3/21
 */
public class SuperClassUtils {

    /**
     * 获取指定类实现的所有接口
     *
     * @param clazz
     * @return
     */
    public static Set<Class<?>> getAllSuperInterface(Class<?> clazz) {
        Set<Class<?>> allInterfaces = getAllInterfaces(clazz);
        if (Objects.isNull(allInterfaces) || Objects.equals(allInterfaces.size(), 0)) {
            return Collections.emptySet();
        }
        Set<Class<?>> allSuperInterfaces = new HashSet<>(allInterfaces);
        // 查询所有接口上级接口
        for (Class<?> aClass : allInterfaces) {
            allSuperInterfaces.addAll(getSuperInterface(aClass));
        }
        return allSuperInterfaces;
    }

    /**
     * 返回上级接口
     *
     * @param clazz
     * @return
     */
    public static Set<Class<?>> getSuperInterface(Class<?> clazz) {
        if (Objects.isNull(clazz)) {
            return Collections.emptySet();
        }
        Set<Class<?>> superClasses = new HashSet<>();
        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces != null && interfaces.length > 0) {
            superClasses.addAll(Arrays.asList(interfaces));
            for (Class<?> anInterface : interfaces) {
                superClasses.addAll(getSuperInterface(anInterface));
            }
        }
        return superClasses;
    }

    /**
     * 获取指定类及其父类继承的所有接口（不包括接口继承的接口），如果传入接口则返回自身
     *
     * @param clazz
     * @return
     */
    public static Set<Class<?>> getAllInterfaces(Class<?> clazz) {
        if (Objects.isNull(clazz)) {
            return Collections.emptySet();
        }
        if (clazz.isInterface()) {
            return Collections.<Class<?>>singleton(clazz);
        }
        Set<Class<?>> interfaces = new LinkedHashSet<Class<?>>();
        Class<?> current = clazz;
        while (current != null) {
            Class<?>[] ifcs = current.getInterfaces();
            for (Class<?> ifc : ifcs) {
                interfaces.addAll(getAllInterfaces(ifc));
            }
            current = current.getSuperclass();
        }
        return interfaces;
    }

    /**
     * 获取指定类继承的所有父类，传入接口返回空
     *
     * @param clazz
     * @return
     */
    public static Set<Class<?>> getAllSupperClass(Class<?> clazz) {
        if (Objects.isNull(clazz) || clazz.isInterface()) {
            return Collections.emptySet();
        }
        Set<Class<?>> superClasses = new LinkedHashSet<>();
        Class<?> current = clazz;
        while (current != null) {
            Class<?> superclass = current.getSuperclass();
            superClasses.add(superclass);
            current = superclass;
        }
        return superClasses;
    }

}
