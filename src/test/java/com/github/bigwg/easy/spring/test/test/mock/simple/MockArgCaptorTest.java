package com.github.bigwg.easy.spring.test.test.mock.simple;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * MockArgCaptorTest
 *
 * @author zhaozhiwei
 * @since 2024/4/28
 */
@RunWith(MockitoJUnitRunner.class)
public class MockArgCaptorTest {

    @Mock
    private MockArgCaptorServiceA mockArgCaptorServiceA;

    @Test
    public void testMockSequence() {
        // 1.调用方法
        String param1 = "MockArgCaptorServiceA.param1";
        String param2 = "MockArgCaptorServiceA.param2";
        mockArgCaptorServiceA.mock(param1, param2);
        // 2.创建 ArgumentCaptor 实例用于捕获参数
        ArgumentCaptor<String> param1Captor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> param2Captor = ArgumentCaptor.forClass(String.class);
        // 3.捕获参数
        Mockito.verify(mockArgCaptorServiceA).mock(param1Captor.capture(), param2Captor.capture());
        // 4.验证参数是否符合预期
        MatcherAssert.assertThat(param1, Matchers.equalTo(param1Captor.getValue()));
        MatcherAssert.assertThat(param2, Matchers.equalTo(param2Captor.getValue()));
    }

}