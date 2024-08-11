package com.github.bigwg.easy.spring.test.mock;

import com.github.bigwg.easy.spring.test.annotation.MockStatic;
import com.github.bigwg.easy.spring.test.annotation.SpyStatic;
import org.mockito.Answers;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockReset;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.TestContext;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Mockito静态类/方法mock解析类
 *
 * @author zhaozhiwei
 * @since 2024/2/25
 */
public class MockStaticParser {

    private Class<?> testClass;

    private final Set<Field> staticFields;

    private final Set<Field> instanceFields;

    private final Map<Field, MockStaticMetadata> fieldMockStaticMetadata;

    private final Set<Field> injectedStaticFields;

    private final Set<Object> injectedTestInstance;

    public MockStaticParser() {
        this.staticFields = new HashSet<>();
        this.instanceFields = new HashSet<>();
        this.fieldMockStaticMetadata = new LinkedHashMap<>();
        this.injectedStaticFields = new HashSet<>();
        this.injectedTestInstance = new HashSet<>();
    }

    public void injectStaticField(TestContext testContext) {
        if (Objects.equals(fieldMockStaticMetadata.size(), 0)) {
            return;
        }
        if (!Objects.equals(testClass, testContext.getTestClass())) {
            return;
        }
        Object testInstance = null;
        try {
            testInstance = testContext.getTestInstance();
        } catch (IllegalStateException e) {
        }
        if (Objects.nonNull(testInstance)) {
            return;
        }
        // 注入静态类/方法mock实例
        fieldMockStaticMetadata.forEach((field, v) -> {
            // 非静态字段不注入
            if (!staticFields.contains(field)) {
                return;
            }
            // 静态属性已注入过可跳过
            if (!injectedStaticFields.add(field)) {
                return;
            }
            ReflectionUtils.makeAccessible(field);
            ReflectionUtils.setField(field, null, v.getMockedStatic());
        });
    }

    public void injectInstanceField(TestContext testContext) {
        if (Objects.equals(fieldMockStaticMetadata.size(), 0)) {
            return;
        }
        if (!Objects.equals(testClass, testContext.getTestClass())) {
            return;
        }
        Object testInstance = null;
        try {
            testInstance = testContext.getTestInstance();
        } catch (IllegalStateException e) {
        }
        if (Objects.isNull(testInstance)) {
            return;
        }
        final Object finalTestInstance = testInstance;
        // 注入静态类/方法mock实例
        fieldMockStaticMetadata.forEach((field, v) -> {
            // 非静态字段注入
            if (!instanceFields.contains(field)) {
                return;
            }
            if (injectedTestInstance.contains(finalTestInstance)) {
                return;
            }
            ReflectionUtils.makeAccessible(field);
            ReflectionUtils.setField(field, finalTestInstance, v.getMockedStatic());
        });
        injectedTestInstance.add(testInstance);
    }

    public void reset(MockReset mockReset) {
        // 重置静态类/方法mock实例
        if (Objects.equals(fieldMockStaticMetadata.size(), 0)) {
            return;
        }
        fieldMockStaticMetadata.forEach((k, v) -> {
            MockedStatic<?> mockedStatic = v.getMockedStatic();
            MockReset mockResetConfig = v.getMockReset();
            if (Objects.equals(mockResetConfig, mockReset)) {
                mockedStatic.reset();
            }
        });
    }

    public void close() {
        // 关闭静态mock
        if (Objects.equals(fieldMockStaticMetadata.size(), 0)) {
            return;
        }
        fieldMockStaticMetadata.forEach((k, v) -> {
            MockedStatic<?> mockedStatic = v.getMockedStatic();
            mockedStatic.close();
        });
    }

    public void parse(Class<?> testClass) {
        this.testClass = testClass;
        ReflectionUtils.doWithFields(testClass, this::parseElement);
    }

    private void parseElement(Field field) {
        Class<?> type = field.getType();
        MockStatic mockStaticAnno = AnnotationUtils.getAnnotation(field, MockStatic.class);
        SpyStatic spyStaticAnno = AnnotationUtils.getAnnotation(field, SpyStatic.class);
        if (!Objects.equals(type, MockedStatic.class)) {
            return;
        }
        if (Objects.isNull(mockStaticAnno) && Objects.isNull(spyStaticAnno)) {
            return;
        }
        Class<?> targetMockClass;
        Answers answers;
        MockReset reset;
        if (Objects.nonNull(spyStaticAnno)) {
            targetMockClass = spyStaticAnno.value();
            answers = Answers.CALLS_REAL_METHODS;
            reset = spyStaticAnno.reset();
        } else {
            targetMockClass = mockStaticAnno.value();
            answers = mockStaticAnno.answers();
            reset = mockStaticAnno.reset();
        }
        MockedStatic<?> mockedStatic = Mockito.mockStatic(targetMockClass, answers);
        MockStaticMetadata mockStaticMetadata = new MockStaticMetadata(targetMockClass, answers,
                reset, mockedStatic);
        boolean staticField = Modifier.isStatic(field.getModifiers());
        if (staticField) {
            staticFields.add(field);
        } else {
            instanceFields.add(field);
        }
        fieldMockStaticMetadata.put(field, mockStaticMetadata);
    }

}
