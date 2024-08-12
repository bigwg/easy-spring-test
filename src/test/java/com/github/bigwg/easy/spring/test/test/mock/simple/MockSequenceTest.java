package com.github.bigwg.easy.spring.test.test.mock.simple;

import lombok.extern.slf4j.Slf4j;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * MockSequenceTest
 *
 * @author zhaozhiwei
 * @since 2024/4/28
 */
@RunWith(MockitoJUnitRunner.class)
public class MockSequenceTest {

    @Mock
    private MockServiceA mockServiceA;

    @Test
    public void testMockSequence() {
        // 1.mock
        String sequenceMockOne = "sequenceMockOne";
        String sequenceMockTwo = "sequenceMockTwo";
        Mockito.when(mockServiceA.mock()).thenReturn(sequenceMockOne).thenReturn(sequenceMockTwo).thenThrow(new RuntimeException());
        // 2.测试
        String invokeMockOne = mockServiceA.mock();
        String invokeMockTwo = mockServiceA.mock();
        // 3.验证
        MatcherAssert.assertThat(invokeMockOne, Matchers.equalTo(sequenceMockOne));
        MatcherAssert.assertThat(invokeMockTwo, Matchers.equalTo(sequenceMockTwo));
        try {
            mockServiceA.mock();
        } catch (Exception e){
            MatcherAssert.assertThat(e, Matchers.instanceOf(RuntimeException.class));
        }
    }

}
