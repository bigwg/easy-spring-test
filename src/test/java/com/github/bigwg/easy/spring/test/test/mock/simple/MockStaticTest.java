package com.github.bigwg.easy.spring.test.test.mock.simple;

import lombok.extern.slf4j.Slf4j;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.MockingDetails;
import org.mockito.Mockito;

/**
 * MockStaticTest
 *
 * @author zhaozhiwei
 * @since 2022/3/3
 */
@Slf4j
public class MockStaticTest {

    private MockInvokeStaticService mockInvokeStaticService = new MockInvokeStaticService();

    @Test
    public void testStaticService() {
        try (MockedStatic<StaticServiceA> mockedStatic = Mockito.mockStatic(StaticServiceA.class)) {
            MockingDetails mockingDetails = Mockito.mockingDetails(StaticServiceA.class);
            try {
                Mockito.mockStatic(StaticServiceA.class);
            } catch (Exception e){
                log.error("eeeee", e);
            }
            System.out.println("in try catch mock: " + mockingDetails.isMock());
            // 打桩
            String mockedReturnStr = "mockedService.invoke";
            mockedStatic.when(StaticServiceA::mock).thenReturn(mockedReturnStr);
            // 测试
            String mock = mockInvokeStaticService.mock();
            // 验证
            MatcherAssert.assertThat(mock, Matchers.equalTo(mockedReturnStr));
        }
        System.out.println("out try catch mock: " + Mockito.mockingDetails(StaticServiceA.class).isMock());
    }

}
