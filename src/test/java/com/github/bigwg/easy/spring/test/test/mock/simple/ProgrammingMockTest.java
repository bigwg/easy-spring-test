package com.github.bigwg.easy.spring.test.test.mock.simple;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

/**
 * ProgrammingMockTest
 *
 * @author zhaozhiwei
 * @since 2022/3/3
 */
public class ProgrammingMockTest {

    private MockInvokeService mockInvokeService = new MockInvokeService();

    @Test
    public void testMockServiceA() throws NoSuchFieldException, IllegalAccessException {
        // mock 并注入被 mock 对象到待测试类
        MockServiceA mockedMockServiceA = Mockito.mock(MockServiceA.class);
        Field field = MockInvokeService.class.getDeclaredField("mockServiceA");
        field.setAccessible(true);
        field.set(mockInvokeService, mockedMockServiceA);
        // 打桩
        String mockedReturnStr = "mockedService.invoke";
        Mockito.doReturn(mockedReturnStr).when(mockedMockServiceA).mock();
        // 测试
        String mock = mockedMockServiceA.mock();
        // 验证
        MatcherAssert.assertThat(mock, Matchers.equalTo(mockedReturnStr));
    }

}
