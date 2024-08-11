package com.github.bigwg.easy.spring.test.suite;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.internal.builders.AllDefaultPossibilitiesBuilder;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 自定义测试套件
 *
 * @author zhaozhiwei
 * @since 2024/6/14
 */
@Slf4j
public class EasySuite extends ParentRunner<Runner> {

    private List<Runner> runners;

    private final Set<Class<?>> excludeClasses = new HashSet<>();

    private final List<Pattern> includePatterns = new LinkedList<>();

    private final List<Pattern> excludePatterns = new LinkedList<>();

    public EasySuite(Class<?> testClass) throws InitializationError {
        super(testClass);
        // 初始化 filter
        initFilter(testClass);
        // 扫描并过滤测试类
        List<Class<?>> scannedTestClass = scanTestClass(testClass);
        // 过滤测试类
        List<Class<?>> filteredTestClasses = Lists.newArrayListWithCapacity(scannedTestClass.size());
        for (Class<?> candidateTestClass : scannedTestClass) {
            if (filter(candidateTestClass)) {
                filteredTestClasses.add(candidateTestClass);
            }
        }
        AllDefaultPossibilitiesBuilder builder = new AllDefaultPossibilitiesBuilder(true);
        this.runners = Collections.unmodifiableList(builder.runners(testClass, filteredTestClasses));
    }

    @Override
    protected List<Runner> getChildren() {
        return runners;
    }

    @Override
    protected Description describeChild(Runner child) {
        return child.getDescription();
    }

    @Override
    protected void runChild(Runner runner, final RunNotifier notifier) {
        runner.run(notifier);
    }

    private void initFilter(Class<?> rootTestClass) {
        // 初始化 ExcludeClass 注解
        ExcludeClass excludeClassAnno = rootTestClass.getAnnotation(ExcludeClass.class);
        if (Objects.nonNull(excludeClassAnno) && excludeClassAnno.value().length != 0) {
            Collections.addAll(excludeClasses, excludeClassAnno.value());
        }
        // 初始化 IncludeClassNamePatterns 注解匹配器
        IncludeClassNamePatterns includeClassNamePatternsAnno = rootTestClass.getAnnotation(IncludeClassNamePatterns.class);
        if (Objects.nonNull(includeClassNamePatternsAnno)) {
            for (String classNamePattern : includeClassNamePatternsAnno.value()) {
                includePatterns.add(Pattern.compile(classNamePattern));
            }
        }
        // 初始化 ExcludeClassNamePatterns 注解匹配器
        ExcludeClassNamePatterns excludeClassNamePatternsAnno = rootTestClass.getAnnotation(ExcludeClassNamePatterns.class);
        if (Objects.nonNull(excludeClassNamePatternsAnno)) {
            for (String classNamePattern : excludeClassNamePatternsAnno.value()) {
                excludePatterns.add(Pattern.compile(classNamePattern));
            }
        }
    }

    private List<Class<?>> scanTestClass(Class<?> rootTestClass) throws InitializationError {
        List<String> scanPackages = new ArrayList<>();
        // 处理 SelectPackages 注解
        SelectPackages selectPackagesAnno = rootTestClass.getAnnotation(SelectPackages.class);
        if (Objects.nonNull(selectPackagesAnno)) {
            scanPackages.addAll(Lists.newArrayList(selectPackagesAnno.value()));
        } else {
            scanPackages.add(rootTestClass.getPackage().getName());
        }
        List<Class<?>> scannedAndIncludeTestClasses = PackageClassScanner.scan(scanPackages);
        // 处理 IncludeClass 注解
        IncludeClass includeClassAnno = rootTestClass.getAnnotation(IncludeClass.class);
        if (Objects.nonNull(includeClassAnno)) {
            Class<?>[] value = includeClassAnno.value();
            if (Objects.nonNull(value) && value.length != 0) {
                scannedAndIncludeTestClasses.addAll(Lists.newArrayList(value));
            }
        }
        return scannedAndIncludeTestClasses;
    }

    /**
     * 过滤测试类
     *
     * @param testClass
     * @return
     */
    private boolean filter(Class<?> testClass) {
        // 判断测试类是否不包含测试方法
        boolean hasTestMethod = false;
        Method[] allDeclaredMethods = ReflectionUtils.getAllDeclaredMethods(testClass);
        if (Objects.isNull(allDeclaredMethods) || allDeclaredMethods.length == 0) {
            return false;
        }
        for (Method method : allDeclaredMethods) {
            Annotation testAnno = method.getAnnotation(Test.class);
            if (Objects.nonNull(testAnno)) {
                hasTestMethod = true;
            }
        }
        if (!hasTestMethod) {
            return false;
        }
        // 判断 ExcludeClass 注解排除的测试类
        if (excludeClasses.contains(testClass)) {
            return false;
        }
        String testClassName = testClass.getName();
        // 判断 IncludeClassNamePatterns 注解包含的测试类
        if (!CollectionUtils.isEmpty(includePatterns)) {
            for (Pattern includePattern : includePatterns) {
                if (!includePattern.matcher(testClassName).matches()) {
                    return false;
                }
            }
        }
        // 判断 ExcludeClassNamePatterns 注解排除的测试类
        if (!CollectionUtils.isEmpty(excludePatterns)) {
            for (Pattern excludePattern : excludePatterns) {
                if (excludePattern.matcher(testClassName).matches()) {
                    return false;
                }
            }
        }
        return true;
    }

}
