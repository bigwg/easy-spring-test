package com.github.bigwg.easy.spring.test.test.context.multiple;

import com.github.bigwg.easy.spring.test.annotation.AutoImport;
import com.github.bigwg.easy.spring.test.test.BaseTest;
import com.github.bigwg.easy.spring.test.test.context.multiple.exclude.MultipleImportExcludeService;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * MultipleImportInTestClassTest
 *
 * @author zhaozhiwei
 * @since 2023/4/26
 */
@AutoImport(scanBasePackages = {"com.github.bigwg.easy.spring.test.test.context.multiple",
        "com.github.bigwg.easy.spring.test.test.context"})
public class MultipleImportInTestClassTest extends BaseTest {

    @Autowired
    private Map<String, MultipleBeanServiceI> multipleBeanServiceIMap;

    @Test
    public void autoImportMultipleTest() {
        MultipleBeanServiceI multipleBeanServiceA = multipleBeanServiceIMap.get("multipleServiceA");
        String helloMultipleBeanServiceA = multipleBeanServiceA.hello();
        MatcherAssert.assertThat(helloMultipleBeanServiceA, Matchers.equalTo("MultipleServiceA"));
        MultipleBeanServiceI multipleBeanServiceB = multipleBeanServiceIMap.get("multipleServiceB");
        String helloMultipleBeanServiceB = multipleBeanServiceB.hello();
        MatcherAssert.assertThat(helloMultipleBeanServiceB, Matchers.equalTo("MultipleServiceB"));
    }

}