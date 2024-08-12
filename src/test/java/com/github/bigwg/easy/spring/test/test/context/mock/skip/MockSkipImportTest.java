package com.github.bigwg.easy.spring.test.test.context.mock.skip;

import com.github.bigwg.easy.spring.test.annotation.AutoImport;
import com.github.bigwg.easy.spring.test.test.BaseTest;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * MockSkipImportTest
 *
 * @author zhaozhiwei
 * @since 2023/11/30
 */
public class MockSkipImportTest extends BaseTest {

    @MockBean
    private MockSkipImportServiceB mockSkipImportServiceB;

    @Autowired
    private MockSkipImportServiceA mockSkipImportServiceA;

    @Test
    public void testMockSkipImportTest() {
        String hello = mockSkipImportServiceA.hello();
        MatcherAssert.assertThat(hello, Matchers.equalTo("MockSkipImportServiceA"));
    }

}
