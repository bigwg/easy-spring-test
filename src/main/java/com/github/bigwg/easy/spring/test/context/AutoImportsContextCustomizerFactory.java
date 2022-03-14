package com.github.bigwg.easy.spring.test.context;

import com.github.bigwg.easy.spring.test.annotation.AutoImport;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.ContextCustomizerFactory;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * AutoImportsContextCustomizerFactory
 *
 * @author zhaozhiwei
 * @since 2022/3/3
 */
public class AutoImportsContextCustomizerFactory implements ContextCustomizerFactory {

    @Override
    public ContextCustomizer createContextCustomizer(Class<?> testClass,
                                                     List<ContextConfigurationAttributes> configAttributes) {
        if (AnnotatedElementUtils.findMergedAnnotation(testClass, AutoImport.class) != null) {
            assertHasNoBeanMethods(testClass);
            return new AutoImportsContextCustomizer(testClass);
        }
        return null;
    }

    private void assertHasNoBeanMethods(Class<?> testClass) {
        ReflectionUtils.doWithMethods(testClass, this::assertHasNoBeanMethods);
    }

    private void assertHasNoBeanMethods(Method method) {
        Assert.state(!AnnotatedElementUtils.isAnnotated(method, Bean.class),
                "Test classes cannot include @Bean methods");
    }

}
