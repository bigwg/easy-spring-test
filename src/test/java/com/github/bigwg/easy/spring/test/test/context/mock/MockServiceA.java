package com.github.bigwg.easy.spring.test.test.context.mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ServiceA
 *
 * @author zhaozhiwei
 * @since 2023/4/25
 */
@Service
public class MockServiceA {
    @Autowired
    private MockServiceB mockServiceB;

    public String hello() {
        return "MockServiceA";
    }

    public String helloMockServiceB() {
        return "invoke MockServiceA.helloMockServiceB: " + mockServiceB.hello();
    }

}
