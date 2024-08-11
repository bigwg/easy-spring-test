package com.github.bigwg.easy.spring.test.test.context.multiple;

import com.github.bigwg.easy.spring.test.annotation.AutoImport;
import com.github.bigwg.easy.spring.test.test.BaseTest;
import com.github.bigwg.easy.spring.test.test.context.multiple.exclude.MultipleImportExcludeService;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * MultipleImportTest
 *
 * @author zhaozhiwei
 * @since 2023/4/26
 */
@AutoImport(scanBasePackages = {"com.github.bigwg.easy.spring.test.test.context.multiple",
        "com.github.bigwg.easy.spring.test.test.context"})
public class MultipleImportTest extends BaseTest {

    @Autowired
    private MultipleImportService multipleImportService;

    @Autowired(required = false)
    private MultipleImportExcludeService multipleImportExcludeService;

    @Test
    public void autoImportMultipleTest() {
        String invokeMultipleServiceA = multipleImportService.invokeMultipleServiceA();
        MatcherAssert.assertThat(invokeMultipleServiceA, Matchers.equalTo("invoke MultipleServiceA: MultipleServiceA"));
        String invokeMultipleServiceB = multipleImportService.invokeMultipleServiceB();
        MatcherAssert.assertThat(invokeMultipleServiceB, Matchers.equalTo("invoke MultipleServiceB: MultipleServiceB"));
    }

}