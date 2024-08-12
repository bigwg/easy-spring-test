package com.github.bigwg.easy.spring.test.mock;

import com.github.bigwg.easy.spring.test.annotation.MockStatic;
import org.springframework.boot.test.mock.mockito.MockReset;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Mockito静态类/方法测试执行Listener
 *
 * @author zhaozhiwei
 * @since 2024/2/25
 */
public class MockitoStaticTestExecutionListener extends AbstractTestExecutionListener {

    /**
     * 静态（类）方法mock是jvm级别全局mock，所以需要使用静态字段并且加锁保证唯一和线程安全
     */
    private static final Map<Class<?>, MockStaticParser> testClass2MockStatic = new HashMap<>();

    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        // 解析静态类/方法mock
        parseMockStatic(testContext);
        // 注入静态类/方法mock实例
        injectStaticField(testContext);
    }

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        // 注入实例字段mock实例
        injectInstanceField(testContext);
        // 重置
        reset(testContext, MockReset.BEFORE);
    }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        // 重置
        reset(testContext, MockReset.AFTER);
    }

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
        MockStaticParser mockStaticParser = testClass2MockStatic.get(testContext.getTestClass());
        if (Objects.isNull(mockStaticParser)) {
            return;
        }
        // 清理静态mock数据
        mockStaticParser.close();
        synchronized (testClass2MockStatic) {
            testClass2MockStatic.remove(testContext.getTestClass());
        }
    }

    @Override
    public int getOrder() {
        return 2950;
    }

    private void injectStaticField(TestContext testContext) {
        // 初始化静态mock对象
        MockStaticParser mockStaticParser = testClass2MockStatic.get(testContext.getTestClass());
        if (Objects.isNull(mockStaticParser)) {
            return;
        }
        mockStaticParser.injectStaticField(testContext);
    }

    private void injectInstanceField(TestContext testContext) {
        // 初始化静态mock对象
        MockStaticParser mockStaticParser = testClass2MockStatic.get(testContext.getTestClass());
        if (Objects.isNull(mockStaticParser)) {
            return;
        }
        mockStaticParser.injectInstanceField(testContext);
    }

    private void reset(TestContext testContext, MockReset mockReset) {
        MockStaticParser mockStaticParser = testClass2MockStatic.get(testContext.getTestClass());
        if (Objects.isNull(mockStaticParser)) {
            return;
        }
        mockStaticParser.reset(mockReset);
    }

    private void parseMockStatic(TestContext testContext) {
        // 包含测试类代表已经解析过，不进行解析
        if (testClass2MockStatic.containsKey(testContext.getTestClass())) {
            return;
        }
        // 测试类没有静态mock注解，不进行解析
        if (!hasMockStaticAnnotations(testContext)) {
            return;
        }
        synchronized (testClass2MockStatic) {
            if (testClass2MockStatic.containsKey(testContext.getTestClass())) {
                return;
            }
            Class<?> testClass = testContext.getTestClass();
            MockStaticParser mockStaticParser = new MockStaticParser();
            mockStaticParser.parse(testClass);
            testClass2MockStatic.put(testClass, mockStaticParser);
        }
    }

    private boolean hasMockStaticAnnotations(TestContext testContext) {
        MockStaticAnnotationCollection collector = new MockStaticAnnotationCollection();
        ReflectionUtils.doWithFields(testContext.getTestClass(), collector);
        return collector.hasAnnotations();
    }

    private static class MockStaticAnnotationCollection implements FieldCallback {

        private final Set<Annotation> annotations = new LinkedHashSet<Annotation>();

        @Override
        public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
            for (Annotation annotation : field.getDeclaredAnnotations()) {
                if (Objects.equals(annotation.annotationType(), MockStatic.class)) {
                    this.annotations.add(annotation);
                }
            }
        }

        public boolean hasAnnotations() {
            return !this.annotations.isEmpty();
        }

    }

}
