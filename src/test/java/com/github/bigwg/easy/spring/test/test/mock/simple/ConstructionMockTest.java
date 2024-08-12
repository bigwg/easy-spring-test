package com.github.bigwg.easy.spring.test.test.mock.simple;

import lombok.extern.slf4j.Slf4j;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

/**
 * ConstructionMockTest
 *
 * @author zhaozhiwei
 * @since 2024/3/11
 */
@Slf4j
public class ConstructionMockTest {

    @Test
    public void testMockConstruction() {
        // 1.mock
        String constructionMock = "constructionMock";
        MockedConstruction<MockServiceA> mockedConstruction = Mockito.mockConstruction(MockServiceA.class,
                (mocked, context) -> {
                    // mocked 为调用构造器后生成的mock对象，context为调用构造器的上下文
                    // 包括构造器context.constructor()和构造器参数context.arguments()，以及构造器被调用次数context.getCount()
                    Mockito.doReturn(constructionMock).when(mocked).mock();
                });

        // 2.测试
        MockConstructionInvokeService mockConstructionInvokeService = new MockConstructionInvokeService();
        String argsMock = mockConstructionInvokeService.mockConstructionWithArgs("test");
        String noArgsMock = mockConstructionInvokeService.mockConstructionNoArgs();
        // 3.验证
        MatcherAssert.assertThat(argsMock, Matchers.equalTo(constructionMock));
        MatcherAssert.assertThat(noArgsMock, Matchers.equalTo(constructionMock));
        mockedConstruction.close();
    }

}
