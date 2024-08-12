package com.github.bigwg.easy.spring.test.h2;

import com.github.bigwg.easy.spring.test.util.BeanDefinitionRegistryUtil;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.ManagedArray;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.MergedContextConfiguration;

import java.util.Objects;

/**
 * H2ForceIndexInterceptorCustomizer
 *
 * @author zhaozhiwei
 * @since 2022/4/6
 */
public class H2ForceIndexInterceptorCustomizer implements ContextCustomizer {

    private static final String DEFAULT_SQL_SESSION_FACTORY_NAME = "sqlSessionFactory";

    @Override
    public void customizeContext(ConfigurableApplicationContext context, MergedContextConfiguration mergedConfig) {
        BeanDefinitionRegistry beanDefinitionRegistry = BeanDefinitionRegistryUtil.getBeanDefinitionRegistry(context);
        if (!beanDefinitionRegistry.containsBeanDefinition(DEFAULT_SQL_SESSION_FACTORY_NAME)) {
            return;
        }
        BeanDefinition ssfBeanDefinition = beanDefinitionRegistry.getBeanDefinition(DEFAULT_SQL_SESSION_FACTORY_NAME);
        MutablePropertyValues propertyValues = ssfBeanDefinition.getPropertyValues();
        Object pluginsObj = propertyValues.get("plugins");
        if (Objects.isNull(pluginsObj)){
            Interceptor[] plugins = new Interceptor[1];
            plugins[0] = new H2ForceIndexInterceptor();
            propertyValues.removePropertyValue("plugins");
            propertyValues.add("plugins", plugins);
            return;
        }
        if (pluginsObj instanceof Interceptor[]){
            Interceptor[] oldPlugins = (Interceptor[]) pluginsObj;
            Interceptor[] newPlugins = new Interceptor[oldPlugins.length + 1];
            newPlugins[0] = new H2ForceIndexInterceptor();
            System.arraycopy(oldPlugins, 0, newPlugins, 1, oldPlugins.length);
            propertyValues.removePropertyValue("plugins");
            propertyValues.add("plugins", newPlugins);
        } else {
            ManagedArray oldPlugins = (ManagedArray) pluginsObj;
            ManagedArray newPlugins = new ManagedArray(oldPlugins.getElementTypeName(), oldPlugins.size() + 1);
            newPlugins.add(new H2ForceIndexInterceptor());
            newPlugins.addAll(oldPlugins);
            propertyValues.removePropertyValue("plugins");
            propertyValues.add("plugins", newPlugins);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
