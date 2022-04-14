package com.github.bigwg.easy.spring.test.h2;

import com.github.bigwg.easy.spring.test.util.ApplicationContextUtil;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.MergedContextConfiguration;

import java.util.Arrays;
import java.util.Objects;

/**
 * H2ForceIndexInterceptorCustomizer
 *
 * @author zhaozhiwei
 * @since 2022/4/6
 */
public class H2ForceIndexInterceptorCustomizer implements ContextCustomizer {

    @Override
    public void customizeContext(ConfigurableApplicationContext context, MergedContextConfiguration mergedConfig) {
        BeanDefinitionRegistry beanDefinitionRegistry = ApplicationContextUtil.getBeanDefinitionRegistry(context);
        BeanDefinition ssfBeanDefinition = beanDefinitionRegistry.getBeanDefinition("sqlSessionFactory");
        MutablePropertyValues propertyValues = ssfBeanDefinition.getPropertyValues();
        Interceptor[] plugins = (Interceptor[]) propertyValues.get("plugins");
        if (Objects.isNull(plugins)) {
            plugins = new Interceptor[1];
            plugins[0] = new H2ForceIndexInterceptor();
            propertyValues.removePropertyValue("plugins");
            propertyValues.add("plugins", plugins);
        } else {
            Interceptor[] newPlugins = Arrays.copyOf(plugins, plugins.length + 1);
            newPlugins[plugins.length] = new H2ForceIndexInterceptor();
            propertyValues.removePropertyValue("plugins");
            propertyValues.add("plugins", newPlugins);
        }
    }

}
