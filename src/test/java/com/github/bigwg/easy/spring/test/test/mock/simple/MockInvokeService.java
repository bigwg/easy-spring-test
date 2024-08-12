package com.github.bigwg.easy.spring.test.test.mock.simple;

/**
 * MockInvokeService
 *
 * @author zhaozhiwei
 * @since 2024/3/8
 */
public class MockInvokeService {

    private MockServiceA mockServiceA;

    public String mock() {
        return mockServiceA.mock();
    }

    public String mockWithArg(String name) {
        return mockServiceA.mock(name);
    }

}
