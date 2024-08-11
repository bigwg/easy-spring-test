package com.github.bigwg.easy.spring.test.test.context.extend;

import com.github.bigwg.easy.spring.test.test.BaseTest;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ExtendImportTest
 *
 * @author zhaozhiwei
 * @since 2022/3/3
 */
public class ExtendImportTest extends BaseTest {

    @Autowired
    private ServiceAImpl serviceAImpl;

    @Test
    public void autoImportExtendTest() {
        String extendServiceA = serviceAImpl.invokeExtendServiceA();
        MatcherAssert.assertThat(extendServiceA, Matchers.equalTo("invoke ExtendServiceA: ExtendServiceA"));
    }

}
