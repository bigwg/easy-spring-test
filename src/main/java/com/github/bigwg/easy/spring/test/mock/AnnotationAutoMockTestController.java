package com.github.bigwg.easy.spring.test.mock;

import com.github.bigwg.easy.spring.test.annotation.AutoMock;
import com.github.bigwg.easy.spring.test.context.AbstractAutoMockTestController;
import com.github.bigwg.easy.spring.test.context.FieldClass;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 自定义类或包路径 自动 mock 控制器
 *
 * @author zhaozhiwei
 * @since 2024/3/3
 */
public class AnnotationAutoMockTestController extends AbstractAutoMockTestController {

    private static final String ANT_SEPARATOR = "/";

    private static final String DOT_SEPARATOR = ".";

    private static final String DEFAULT_RESOURCE_PATTERN = "**/*";

    private AutoMock autoMockAnnotation;
    AntPathMatcher matcher = new AntPathMatcher();
    private final Set<String> classNames = new HashSet<>();
    private final List<String> packagePaths = new LinkedList<>();

    @Override
    public void prepareController(Class<?> testClass, BeanDefinitionRegistry registry) {
        super.prepareController(testClass, registry);
        autoMockAnnotation = AnnotationUtils.findAnnotation(testClass, AutoMock.class);
        if (Objects.nonNull(autoMockAnnotation)) {
            Class<?>[] value = autoMockAnnotation.value();
            for (Class<?> aClass : value) {
                classNames.add(aClass.getName());
            }
            String[] basePackages = autoMockAnnotation.scanBasePackages();
            for (String basePackage : basePackages) {
                if (!StringUtils.isEmpty(basePackage)) {
                    String basePackageAnt = convertResourcePath(basePackage);
                    String packagePath;
                    if (basePackageAnt.endsWith(ANT_SEPARATOR)) {
                        packagePath = convertResourcePath(basePackage) + DEFAULT_RESOURCE_PATTERN;
                    } else {
                        packagePath = convertResourcePath(basePackage) + ANT_SEPARATOR + DEFAULT_RESOURCE_PATTERN;
                    }
                    packagePaths.add(packagePath);
                }
            }
            packagePaths.addAll(Arrays.asList(autoMockAnnotation.scanBasePackages()));
            enabled = true;
        }
    }

    @Override
    protected boolean checkAutoMock(FieldClass fieldClass) {
        Class<?> clazz = fieldClass.getClazz();
        if (classNames.contains(clazz.getName())) {
            return true;
        }
        String simpleName = clazz.getName();
        for (String packagePath : packagePaths) {
            if (matcher.matchStart(packagePath, convertResourcePath(simpleName))) {
                return true;
            }
        }
        return false;
    }

    private String convertResourcePath(String path) {
        return path.replace(DOT_SEPARATOR, ANT_SEPARATOR);
    }
}
