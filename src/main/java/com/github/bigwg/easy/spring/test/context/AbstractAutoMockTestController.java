package com.github.bigwg.easy.spring.test.context;

import com.github.bigwg.easy.spring.test.util.SpringBeanUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.mockito.Answers;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.test.mock.mockito.MockReset;
import org.springframework.boot.test.mock.mockito.MockitoPostProcessor;
import org.springframework.core.ResolvableType;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 自动 mock 测试控制器抽象类
 *
 * @author zhaozhiwei
 * @since 2024/3/3
 */
@Slf4j
public abstract class AbstractAutoMockTestController implements AutoMockTestController {

    protected boolean enabled;

    protected Class<?> testClass;
    protected BeanDefinitionRegistry registry;

    private Set<FieldClass> needAutoMockBeans = new HashSet<>();

    private static final String MOCK_DEFINITION_NAME = "org.springframework.boot.test.mock.mockito.MockDefinition";

    private static final String QUALIFIER_DEFINITION_NAME = "org.springframework.boot.test.mock.mockito.QualifierDefinition";

    @Override
    public boolean enabled() {
        return enabled;
    }

    @Override
    public void prepareController(Class<?> testClass, BeanDefinitionRegistry registry) {
        this.testClass = testClass;
        this.registry = registry;
    }

    @Override
    public boolean needAutoMock(FieldClass fieldClass) {
        if (checkAutoMock(fieldClass)) {
            needAutoMockBeans.add(fieldClass);
            return true;
        }
        return false;
    }

    protected abstract boolean checkAutoMock(FieldClass fieldClass);

    @Override
    public void autoMock() {
        Class<?> mockDefinition;
        Class<?> qualifierDefinition;
        try {
            mockDefinition = ClassUtils.forName(MOCK_DEFINITION_NAME, getClass().getClassLoader());
            qualifierDefinition = ClassUtils.forName(QUALIFIER_DEFINITION_NAME, getClass().getClassLoader());
        } catch (ClassNotFoundException e) {
            log.warn("未找到 Mockito 组件，自动生成 mock bean 未生效", e);
            return;
        }
        List<MockClass> mockClasses = processMergedDefinitions(needAutoMockBeans);
        if (CollectionUtils.isEmpty(mockClasses)) {
            return;
        }
        String beanName = MockitoPostProcessor.class.getName();
        BeanDefinition definition;
        if (!registry.containsBeanDefinition(beanName)) {
            RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(MockitoPostProcessor.class);
            rootBeanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
            ConstructorArgumentValues constructorArguments = rootBeanDefinition.getConstructorArgumentValues();
            constructorArguments.addIndexedArgumentValue(0, new LinkedHashSet<>());
            registry.registerBeanDefinition(beanName, rootBeanDefinition);
            definition = rootBeanDefinition;
        } else {
            definition = registry.getBeanDefinition(beanName);
        }
        Set definitions = new HashSet();
        try {
            for (MockClass mockClass : mockClasses) {
                Constructor<?> declaredConstructor = mockDefinition.getDeclaredConstructor(String.class, ResolvableType.class, Class[].class,
                        Answers.class, boolean.class, MockReset.class, qualifierDefinition);
                Object o = BeanUtils.instantiateClass(declaredConstructor, mockClass.getName(), ResolvableType.forClass(mockClass.getClazz()),
                        new Class[]{}, Answers.RETURNS_DEFAULTS, false, MockReset.AFTER, null);
                definitions.add(o);
            }
            ConstructorArgumentValues.ValueHolder constructorArg = definition.getConstructorArgumentValues().getIndexedArgumentValue(0, Set.class);
            Set<?> existing = (Set<?>) constructorArg.getValue();

            if (definitions.size() > 0) {
                existing.addAll(definitions);
            }
        } catch (Exception e) {
            log.error("自动生成 mock bean 失败，请及时查看, needAutoMockBeans:{}", needAutoMockBeans, e);
            throw new RuntimeException(e);
        }
    }

    protected List<MockClass> processMergedDefinitions(Set<FieldClass> fieldClasses) {
        Set<Class<?>> noSpecifyBeanName = new HashSet<>();
        Set<String> specifyBeanName = new HashSet<>();
        List<MockClass> result = new LinkedList<>();
        if (fieldClasses.isEmpty()) {
            return Collections.emptyList();
        }
        for (FieldClass fieldClass : fieldClasses) {
            String beanName = SpringBeanUtils.getInjectedBeanName(fieldClass);
            Class<?> clazz = fieldClass.getClazz();
            if (StringUtils.hasLength(beanName)) {
                if (specifyBeanName.add(beanName)) {
                    result.add(new MockClass(beanName, clazz));
                }
            } else {
                if (noSpecifyBeanName.add(clazz)) {
                    result.add(new MockClass(beanName, clazz));
                }
            }
        }
        return result;
    }

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MockClass {
        private String name;
        private Class<?> clazz;
    }

}
