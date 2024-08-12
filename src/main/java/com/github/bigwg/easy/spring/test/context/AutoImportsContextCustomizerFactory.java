package com.github.bigwg.easy.spring.test.context;

import com.github.bigwg.easy.spring.test.annotation.AutoImport;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.ContextCustomizerFactory;

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
            return new AutoImportsContextCustomizer(testClass);
        }
        return null;
    }

}
