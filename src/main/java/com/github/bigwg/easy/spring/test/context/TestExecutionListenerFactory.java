package com.github.bigwg.easy.spring.test.context;

import org.springframework.test.context.TestExecutionListener;

import java.util.List;

/**
 * TestExecutionListenerFactory
 *
 * @author zhaozhiwei
 * @since 2024/4/22
 */
public interface TestExecutionListenerFactory {

    /**
     * 创建 TestExecutionListeners
     *
     * @param testClass 测试类
     * @return
     */
    List<TestExecutionListener> createTestExecutionListeners(Class<?> testClass);

}
