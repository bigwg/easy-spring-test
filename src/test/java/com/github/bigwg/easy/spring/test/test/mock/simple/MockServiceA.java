package com.github.bigwg.easy.spring.test.test.mock.simple;

/**
 * MockServiceA
 *
 * @author zhaozhiwei
 * @since 2024/3/8
 */
public class MockServiceA {

    private String name = "default";

    public MockServiceA() {
    }

    public MockServiceA(String name) {
        this.name = name;
    }

    public String mock() {
        return name + "MockServiceA.mock";
    }

    public String mock(String name) {
        return name + "MockServiceA.mock";
    }

}
