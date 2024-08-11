package com.github.bigwg.easy.spring.test.suite;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

/**
 * 包路径下类扫描器
 *
 * @author zhaozhiwei
 * @since 2024/6/15
 */
@Slf4j
public class PackageClassScanner {

    public static List<Class<?>> scan(List<String> packageNames) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (CollectionUtils.isEmpty(packageNames)) {
            return Collections.emptyList();
        }
        List<Class<?>> scannedClasses = new ArrayList<>();
        for (String packageName : packageNames) {
            String path = packageName.replace('.', '/');
            Enumeration<URL> resources;
            try {
                resources = classLoader.getResources(path);
            } catch (IOException e) {
                log.error("当前包路径不存在，已忽略，packageName: {}", packageName);
                continue;
            }
            List<Class<?>> classes = new ArrayList<>();
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                File file = new File(resource.getFile());
                classes.addAll(findClasses(packageName, file));
            }
            scannedClasses.addAll(classes);
        }
        return scannedClasses;
    }

    private static List<Class<?>> findClasses(String packageName, File directory) {
        if (!directory.exists()) {
            return Collections.emptyList();
        }
        File[] files = directory.listFiles();
        if (Objects.isNull(files)) {
            return Collections.emptyList();
        }
        List<Class<?>> classes = new ArrayList<>();
        for (File file : files) {
            if (file.isDirectory()) {
                classes.addAll(findClasses(packageName + "." + file.getName(), file));
                continue;
            }
            if (file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                Class<?> clazz;
                try {
                    clazz = Class.forName(className);
                } catch (ClassNotFoundException e) {
                    continue;
                }
                classes.add(clazz);
            }
        }
        return classes;
    }

}
