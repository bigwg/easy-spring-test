package com.github.bigwg.easy.spring.test.test.context.mock;

import org.springframework.stereotype.Service;

/**
 * ServiceB
 *
 * @author zhaozhiwei
 * @since 2023/4/25
 */
@Service("mockServiceC")
public class MockServiceC {

    public String hello() {
        return "MockServiceC";
    }

}
