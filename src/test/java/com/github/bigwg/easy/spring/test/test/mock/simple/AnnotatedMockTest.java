package com.github.bigwg.easy.spring.test.test.mock.simple;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * AnnotatedMockTest
 *
 * @author zhaozhiwei
 * @since 2022/3/3
 */
@RunWith(MockitoJUnitRunner.class)
public class AnnotatedMockTest {

    @Mock
    private MockServiceA mockServiceA;
    @InjectMocks
    private MockInvokeService mockInvokeService = new MockInvokeService();

    @Test
    public void testMockServiceA() {
        // 打桩
        String mockedReturnStr = "mockedService.invoke";
        Mockito.doReturn(mockedReturnStr).when(mockServiceA).mock();
        // 验证方法从未被调用过
        Mockito.verify(mockServiceA, Mockito.never()).mock();
        // 测试
        String mock1 = mockInvokeService.mock();
        String mock2 = mockInvokeService.mock();
        // 验证
        // 验证方法被调用了准确的 n 次
        Mockito.verify(mockServiceA, Mockito.times(2)).mock();
        // 验证方法至少被调用了 n 次
        Mockito.verify(mockServiceA, Mockito.atLeast(1)).mock();
        // 验证方法最多被调用了 n 次
        Mockito.verify(mockServiceA, Mockito.atMost(3)).mock();
        // 验证方法至少被调用了一次
        Mockito.verify(mockServiceA, Mockito.atLeastOnce()).mock();
    }

}
