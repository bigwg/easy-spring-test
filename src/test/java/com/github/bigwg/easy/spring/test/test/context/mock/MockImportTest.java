package com.github.bigwg.easy.spring.test.test.context.mock;

import com.github.bigwg.easy.spring.test.test.BaseTest;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * MockImportTest
 *
 * @author zhaozhiwei
 * @since 2022/3/3
 */
public class MockImportTest extends BaseTest {

    @MockBean
    private MockServiceC mockServiceC;
    @Autowired
    private MockServiceA mockServiceA;
    @Autowired
    private MockServiceB mockServiceB;

    @Test
    public void mockImportTest() {
        String hello = mockServiceA.hello();
        MatcherAssert.assertThat(hello, Matchers.equalTo("MockServiceA"));
        String helloMockServiceB = mockServiceA.helloMockServiceB();
        MatcherAssert.assertThat(helloMockServiceB, Matchers.equalTo("invoke MockServiceA.helloMockServiceB: MockServiceB"));
        Mockito.when(mockServiceC.hello()).thenReturn("MockMockServiceC");
        String invokeServiceC = mockServiceB.helloMockServiceC();
        MatcherAssert.assertThat(invokeServiceC, Matchers.equalTo("invoke MockServiceB.helloMockServiceC: MockMockServiceC"));
    }

    @Test
    public void mockImportRepeatTest() {
        Mockito.when(mockServiceC.hello()).thenReturn("mockImportRepeatTest");
        String invokeServiceC = mockServiceB.helloMockServiceC();
        MatcherAssert.assertThat(invokeServiceC, Matchers.equalTo("invoke MockServiceB.helloMockServiceC: mockImportRepeatTest"));
    }

}
