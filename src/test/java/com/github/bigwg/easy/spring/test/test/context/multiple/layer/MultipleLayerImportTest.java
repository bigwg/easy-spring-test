package com.github.bigwg.easy.spring.test.test.context.multiple.layer;

import com.github.bigwg.easy.spring.test.annotation.AutoImport;
import com.github.bigwg.easy.spring.test.test.BaseTest;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * MultipleLayerImportTest
 *
 * @author zhaozhiwei
 * @since 2023/9/7
 */
@AutoImport(value = MultipleLayerImportService.class,
        scanBasePackages = {"com.github.bigwg.easy.spring.test.test.context"})
public class MultipleLayerImportTest extends BaseTest {

    @Autowired
    private MultipleLayerImportService multipleLayerImportService;

    @Test
    public void autoImportMultipleTest() {
        String invokeMultipleServiceA = multipleLayerImportService.invokeMultipleLayerServiceA();
        MatcherAssert.assertThat(invokeMultipleServiceA, Matchers.equalTo("invoke MultipleLayerServiceA: MultipleLayerServiceA"));
        String invokeMultipleServiceB = multipleLayerImportService.invokeMultipleLayerServiceB();
        MatcherAssert.assertThat(invokeMultipleServiceB, Matchers.equalTo("invoke MultipleLayerServiceB: MultipleLayerServiceB"));
        String invokeAMultipleServiceA = multipleLayerImportService.invokeAMultipleServiceA();
        MatcherAssert.assertThat(invokeAMultipleServiceA, Matchers.equalTo("invoke MultipleLayerServiceA.helloMultipleServiceA: MultipleServiceA"));
        String invokeAMultipleServiceB = multipleLayerImportService.invokeAMultipleServiceB();
        MatcherAssert.assertThat(invokeAMultipleServiceB, Matchers.equalTo("invoke MultipleLayerServiceA.helloMultipleServiceB: MultipleServiceB"));
        String invokeBMultipleServiceA = multipleLayerImportService.invokeBMultipleServiceA();
        MatcherAssert.assertThat(invokeBMultipleServiceA, Matchers.equalTo("invoke MultipleLayerServiceB.helloMultipleServiceA: MultipleServiceA"));
        String invokeBMultipleServiceB = multipleLayerImportService.invokeBMultipleServiceB();
        MatcherAssert.assertThat(invokeBMultipleServiceB, Matchers.equalTo("invoke MultipleLayerServiceB.helloMultipleServiceB: MultipleServiceB"));
    }

}
