package com.github.bigwg.easy.spring.test.test.context.mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ServiceB
 *
 * @author zhaozhiwei
 * @since 2023/4/25
 */
@Service
public class MockServiceB {

    @Autowired
    private MockServiceC mockServiceC;

    public String hello() {
        return "MockServiceB";
    }

    public String helloMockServiceC() {
        return "invoke MockServiceB.helloMockServiceC: " + mockServiceC.hello();
    }

}
