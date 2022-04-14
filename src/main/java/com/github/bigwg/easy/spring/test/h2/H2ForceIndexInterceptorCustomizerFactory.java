package com.github.bigwg.easy.spring.test.h2;

import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.ContextCustomizerFactory;

import java.util.List;

/**
 * H2ForceIndexInterceptorCustomizerFactory
 *
 * @author zhaozhiwei
 * @since 2022/4/6
 */
public class H2ForceIndexInterceptorCustomizerFactory implements ContextCustomizerFactory {

    @Override
    public ContextCustomizer createContextCustomizer(Class<?> testClass, List<ContextConfigurationAttributes> configAttributes) {
        return new H2ForceIndexInterceptorCustomizer();
    }

}
