package com.github.bigwg.easy.spring.test.context;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;

/**
 * 自动 mock 测试控制器
 *
 * @author zhaozhiwei
 * @since 2024/3/3
 */
public interface AutoMockTestController {

    boolean enabled();

    void prepareController(Class<?> testClass, BeanDefinitionRegistry registry);

    boolean needAutoMock(FieldClass fieldClass);

    void autoMock();

}
