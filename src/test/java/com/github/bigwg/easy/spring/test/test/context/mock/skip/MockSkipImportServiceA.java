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
public class MockSkipImportServiceA {

    @Autowired
    private MockServiceB mockServiceB;
    @Autowired
    private MockSkipImportServiceB mockSkipImportServiceB;

    public String hello() {
        return "MockSkipImportServiceA";
    }

    public String helloMockServiceB() {
        return "invoke MockSkipImportServiceA.helloMockServiceB: " + mockServiceB.hello();
    }

    public String helloMockSkipImportServiceB() {
        return "invoke MockSkipImportServiceA.helloMockSkipImportServiceB: " + mockSkipImportServiceB.hello();
    }

}
