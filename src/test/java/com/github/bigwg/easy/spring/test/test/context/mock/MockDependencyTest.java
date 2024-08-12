package com.github.bigwg.easy.spring.test.test.context.mock;

import com.github.bigwg.easy.spring.test.annotation.AutoImport;
import com.github.bigwg.easy.spring.test.test.BaseTest;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * MockDependencyTest
 *
 * @author zhaozhiwei
 * @since 2023/9/7
 */
@AutoImport(MockServiceA.class)
public class MockDependencyTest extends BaseTest implements ApplicationContextAware {

    @MockBean
    private MockServiceB mockServiceB;
    @Autowired
    private MockServiceA mockServiceA;

    private ApplicationContext applicationContext;

    @Test
    public void mockDependencyTest() {
        String hello = mockServiceA.hello();
        MatcherAssert.assertThat(hello, Matchers.equalTo("MockServiceA"));
        String helloMockServiceB = mockServiceA.helloMockServiceB();
        MatcherAssert.assertThat(helloMockServiceB, Matchers.equalTo("invoke MockServiceA.helloMockServiceB: null"));
        try {
            MockServiceC mockServiceC = applicationContext.getBean(MockServiceC.class);
        } catch (Exception e) {
            MatcherAssert.assertThat(e.getClass(), Matchers.equalTo(NoSuchBeanDefinitionException.class));
        }
        Mockito.when(mockServiceB.helloMockServiceC()).thenReturn("MockHelloMockServiceC");
        String helloMockServiceC = mockServiceB.helloMockServiceC();
        MatcherAssert.assertThat(helloMockServiceC, Matchers.equalTo("MockHelloMockServiceC"));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
