package com.github.bigwg.easy.spring.test.mock;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.mockito.Answers;
import org.mockito.MockedStatic;
import org.springframework.boot.test.mock.mockito.MockReset;

/**
 * Mockito静态类/方法元数据
 *
 * @author zhaozhiwei
 * @since 2024/2/25
 */
@Getter
@Setter
@ToString
public class MockStaticMetadata {

    private Class<?> targetMockClass;

    private Answers answers;

    private MockReset mockReset;

    private MockedStatic<?> mockedStatic;

    public MockStaticMetadata(Class<?> targetMockClass, Answers answers, MockReset mockReset, MockedStatic<?> mockedStatic) {
        this.targetMockClass = targetMockClass;
        this.answers = answers;
        this.mockReset = mockReset;
        this.mockedStatic = mockedStatic;
    }

}
