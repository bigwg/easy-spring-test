package com.github.bigwg.easy.spring.test.test.context.mock.skip;

import com.github.bigwg.easy.spring.test.test.context.mock.MockServiceB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ServiceA
 *
 * @author zhaozhiwei
 * @since 2023/4/25
 */
@Service
public class MockSkipImportServiceB {

    @Autowired
    private MockServiceB mockServiceB;
    @Autowired
    private MockSkipServiceI mockSkipService;

    public String hello() {
        return "MockSkipImportServiceB";
    }

    public String helloMockServiceB() {
        return "invoke MockSkipImportServiceB.helloMockServiceB: " + mockServiceB.hello();
    }

}
