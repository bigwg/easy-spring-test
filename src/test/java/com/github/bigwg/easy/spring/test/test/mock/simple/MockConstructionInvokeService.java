package com.github.bigwg.easy.spring.test.test.mock.simple;

/**
 * MockConstructionInvokeService
 *
 * @author zhaozhiwei
 * @since 2024/3/8
 */
public class MockConstructionInvokeService {

    public String mockConstructionNoArgs() {
        MockServiceA mockServiceA = new MockServiceA();
        return mockServiceA.mock();
    }

    public String mockConstructionWithArgs(String name) {
        MockServiceA mockServiceA = new MockServiceA(name);
        return mockServiceA.mock();
    }

}
