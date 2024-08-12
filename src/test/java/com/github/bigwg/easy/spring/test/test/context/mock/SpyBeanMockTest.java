package com.github.bigwg.easy.spring.test.test.context.mock;

import com.github.bigwg.easy.spring.test.test.BaseTest;
import com.github.bigwg.easy.spring.test.test.context.ServiceA;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.SpyBean;

/**
 * SpyBeanMockTest
 *
 * @author zhaozhiwei
 * @since 2023/5/9
 */
public class SpyBeanMockTest extends BaseTest {

    @SpyBean
    private ServiceA serviceA;

    @Test
    public void whenThenMockTest() {
        String hello = serviceA.hello();
        MatcherAssert.assertThat(hello, Matchers.equalTo("ServiceA"));
        Mockito.when(serviceA.hello()).thenReturn("MockServiceA");
        String mockServiceA = serviceA.hello();
        MatcherAssert.assertThat(mockServiceA, Matchers.equalTo("MockServiceA"));
    }

}
