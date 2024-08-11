package com.github.bigwg.easy.spring.test.test.context;

import com.github.bigwg.easy.spring.test.test.BaseTest;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * AutoImportTest
 *
 * @author zhaozhiwei
 * @since 2022/3/3
 */
public class AutoImportTest extends BaseTest {

    @Autowired
    private ServiceA serviceA;

    @Test
    public void autoImportInvokeTest() {
        String hello = serviceA.hello();
        MatcherAssert.assertThat(hello, Matchers.equalTo("ServiceA"));
        String invokeServiceB = serviceA.invokeServiceB();
        MatcherAssert.assertThat(invokeServiceB, Matchers.equalTo("invoke ServiceB: ServiceB"));
        String invokeServiceC = serviceA.invokeServiceC();
        MatcherAssert.assertThat(invokeServiceC, Matchers.equalTo("invoke ServiceC: ServiceC"));
    }

}
