package com.github.bigwg.easy.spring.test.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.test.context.BootstrapContext;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestContextBootstrapper;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.test.context.support.DefaultTestContextBootstrapper;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * SmartSpringTestContextBootstrapper
 *
 * @author zhaozhiwei
 * @since 2024/3/20
 */
@Slf4j
public class SmartSpringTestContextBootstrapper implements TestContextBootstrapper {

    private static final DefaultTestContextBootstrapper DEFAULT_TEST_CONTEXT_BOOTSTRAPPER = new DefaultTestContextBootstrapper();

    @Override
    public void setBootstrapContext(BootstrapContext bootstrapContext) {
        DEFAULT_TEST_CONTEXT_BOOTSTRAPPER.setBootstrapContext(bootstrapContext);
    }

    @Override
    public BootstrapContext getBootstrapContext() {
        return DEFAULT_TEST_CONTEXT_BOOTSTRAPPER.getBootstrapContext();
    }

    @Override
    public TestContext buildTestContext() {
        return DEFAULT_TEST_CONTEXT_BOOTSTRAPPER.buildTestContext();
    }

    @Override
    public MergedContextConfiguration buildMergedContextConfiguration() {
        return DEFAULT_TEST_CONTEXT_BOOTSTRAPPER.buildMergedContextConfiguration();
    }

    @Override
    public List<TestExecutionListener> getTestExecutionListeners() {
        List<TestExecutionListener> testExecutionListeners = DEFAULT_TEST_CONTEXT_BOOTSTRAPPER.getTestExecutionListeners();
        // 加载 TestExecutionListenerFactory
        ClassLoader classLoader = getClass().getClassLoader();
        List<String> classNames =
                SpringFactoriesLoader.loadFactoryNames(TestExecutionListenerFactory.class, classLoader);
        if (!CollectionUtils.isEmpty(classNames)) {
            Class<?> testClass = DEFAULT_TEST_CONTEXT_BOOTSTRAPPER.getBootstrapContext().getTestClass();
            log.info("Loaded TestExecutionListenerFactory class names from location [{}]: {}",
                    SpringFactoriesLoader.FACTORIES_RESOURCE_LOCATION, classNames);
            for (String className : classNames) {
                try {
                    Class<?> clazz = ClassUtils.forName(className, classLoader);
                    TestExecutionListenerFactory factory = (TestExecutionListenerFactory) BeanUtils.instantiateClass(clazz);
                    testExecutionListeners.addAll(factory.createTestExecutionListeners(testClass));
                } catch (Exception e) {
                    log.warn("未找到 TestExecutionListenerFactory: {}, 不加载对应 TestExecutionListener.", className);
                }
            }
        }
        // 根据 order 进行排序
        AnnotationAwareOrderComparator.sort(testExecutionListeners);
        return testExecutionListeners;
    }
}
