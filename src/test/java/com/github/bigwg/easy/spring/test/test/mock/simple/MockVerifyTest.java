package com.github.bigwg.easy.spring.test.test.mock.simple;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * MockVerifyTest
 *
 * @author zhaozhiwei
 * @since 2022/3/3
 */
@RunWith(MockitoJUnitRunner.class)
public class MockVerifyTest {

    @Mock
    private MockServiceA mockServiceA;

    @Test
    public void testMockServiceA() {
        // mock
        String mockedReturnStr = "mockedService.invoke";
        Mockito.doReturn(mockedReturnStr).when(mockServiceA).mock();
        // 测试
        mockServiceA.mock();
        mockServiceA.mock("test");
        // 验证
        // 验证方法被调用顺序
        InOrder inOrder = Mockito.inOrder(mockServiceA);
        inOrder.verify(mockServiceA).mock();
        inOrder.verify(mockServiceA).mock(Mockito.anyString());
    }

}
