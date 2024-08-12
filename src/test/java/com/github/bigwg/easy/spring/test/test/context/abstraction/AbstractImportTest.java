package com.github.bigwg.easy.spring.test.test.context.abstraction;

import com.github.bigwg.easy.spring.test.annotation.AutoImport;
import com.github.bigwg.easy.spring.test.test.BaseTest;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * MockImportTest
 *
 * @author zhaozhiwei
 * @since 2022/3/3
 */
@AutoImport(AbstractServiceImpl.class)
public class AbstractImportTest extends BaseTest {

    @Autowired
    private AbstractServiceImpl abstractService;

    @Test
    public void mockAbstractServiceTest() {
        String hello = abstractService.hello();
        MatcherAssert.assertThat(hello, Matchers.equalTo("ServiceC"));
    }

}
