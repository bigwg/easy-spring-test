package com.github.bigwg.easy.spring.test.context;

import com.github.bigwg.easy.spring.test.annotation.AutoImport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * AutoImportsContextCustomizer
 *
 * @author zhaozhiwei
 * @since 2022/3/3
 */
public class AutoImportsContextCustomizer implements ContextCustomizer {

    private final Class<?> testClass;

    private Set<Class<?>> needImportBeans = new HashSet<>();

    private Set<Class<?>> scannedBeans = new HashSet<>();

    AutoImportsContextCustomizer(Class<?> testClass) {
        this.testClass = testClass;
    }

    @Override
    public void customizeContext(ConfigurableApplicationContext context, MergedContextConfiguration mergedConfig) {
        AutoImport annotation = AnnotationUtils.findAnnotation(testClass, AutoImport.class);
        if (Objects.isNull(annotation)) {
            return;
        }
        Class<?>[] value = annotation.value();
        if (value.length <= 0) {
            return;
        }
        try {
            for (Class<?> aClass : value) {
                this.needImportBeans.addAll(getNeedImportBeans(aClass));
            }
            Set<Class<?>> finalImportBeans = removeMockBeans(testClass, needImportBeans);
            BeanDefinitionRegistry beanDefinitionRegistry = getBeanDefinitionRegistry(context);
            for (Class<?> finalImportBean : finalImportBeans) {
                AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(finalImportBean).getBeanDefinition();
                String beanName = BeanDefinitionReaderUtils.generateBeanName(beanDefinition, beanDefinitionRegistry);
                beanDefinitionRegistry.registerBeanDefinition(beanName, beanDefinition);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private BeanDefinitionRegistry getBeanDefinitionRegistry(ApplicationContext context) {
        if (context instanceof BeanDefinitionRegistry) {
            return (BeanDefinitionRegistry) context;
        }
        if (context instanceof AbstractApplicationContext) {
            return (BeanDefinitionRegistry) ((AbstractApplicationContext) context).getBeanFactory();
        }
        throw new IllegalStateException("Could not locate BeanDefinitionRegistry");
    }

    private Set<Class<?>> getNeedImportBeans(Class<?> clazz) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        if (scannedBeans.contains(clazz) || clazz.isInterface()
                || isDependOnJarClass(clazz)) {
            return Collections.emptySet();
        }
        scannedBeans.add(clazz);
        Set<Class<?>> importBeans = new HashSet<>();
        importBeans.add(clazz);
        Field[] fields = getFields(clazz);
        if (Objects.isNull(fields) || fields.length <= 0) {
            return Collections.emptySet();
        }
        for (Field field : fields) {
            Autowired autowired = field.getAnnotation(Autowired.class);
            Resource resource = field.getAnnotation(Resource.class);
            Class<?> type = field.getType();
            if (Objects.nonNull(autowired) || Objects.nonNull(resource)) {
                importBeans.addAll(getNeedImportBeans(type));
            }
        }
        return importBeans;
    }

    private boolean isDependOnJarClass(Class<?> clazz) {
        String path = clazz.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (path.contains(".m2") || path.contains("repository") || path.contains("lib")) {
            return true;
        }
        return false;
    }

    private Set<Class<?>> removeMockBeans(Class<?> clazz, Set<Class<?>> importBeans) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        Field[] fields = getFields(clazz);
        if (Objects.isNull(fields) || fields.length <= 0) {
            return importBeans;
        }
        List<Class<?>> removeBeans = new LinkedList<>();
        for (Field field : fields) {
            MockBean mockBean = field.getAnnotation(MockBean.class);
            if (Objects.nonNull(mockBean)) {
                removeBeans.add(field.getType());
            }
        }
        importBeans.removeAll(removeBeans);
        return importBeans;
    }

    private Field[] getFields(Class<?> clazz) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        Method getDeclaredFields = ReflectionUtils.class.getDeclaredMethod("getDeclaredFields", Class.class);
        getDeclaredFields.setAccessible(true);
        return (Field[]) getDeclaredFields.invoke(ReflectionUtils.class, clazz);
    }

}
