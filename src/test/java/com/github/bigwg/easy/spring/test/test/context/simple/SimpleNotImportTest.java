package com.github.bigwg.easy.spring.test.test.context.simple;

import com.github.bigwg.easy.spring.test.annotation.AutoImport;
import com.github.bigwg.easy.spring.test.test.BaseTest;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * SimpleNotImportTest
 *
 * @author zhaozhiwei
 * @since 2022/3/3
 */
@AutoImport(SimpleService.class)
public class SimpleNotImportTest extends BaseTest {

    @Autowired
    private SimpleService simpleService;

    @Test
    public void simpleNotImportTest() {
        String hello = simpleService.hello();
        MatcherAssert.assertThat(hello, Matchers.equalTo("SimpleService"));
        String helloSimpleServiceA = simpleService.helloSimpleServiceA();
        MatcherAssert.assertThat(helloSimpleServiceA, Matchers.equalTo("invoke SimpleService.helloSimpleServiceA: SimpleServiceA"));
        String simpleThrown = "Not throw NullPointerException";
        try {
            simpleService.helloSimpleServiceB();
        } catch (Exception e) {
            simpleThrown = "Throw an NullPointerException of simple";
            MatcherAssert.assertThat(e.getClass(), Matchers.equalTo(NullPointerException.class));
        }
        MatcherAssert.assertThat("Throw an NullPointerException of simple", Matchers.equalTo(simpleThrown));
        String abstractThrown = "Not throw NullPointerException";
        try {
            simpleService.helloAbstractService();
        } catch (Exception e) {
            abstractThrown = "Throw an NullPointerException of abstract";
            MatcherAssert.assertThat(e.getClass(), Matchers.equalTo(NullPointerException.class));
        }
        MatcherAssert.assertThat("Throw an NullPointerException of abstract", Matchers.equalTo(abstractThrown));
        String interfaceThrown = "Not throw NullPointerException";
        try {
            simpleService.helloInterfaceService();
        } catch (Exception e) {
            interfaceThrown = "Throw an NullPointerException of interface";
            MatcherAssert.assertThat(e.getClass(), Matchers.equalTo(NullPointerException.class));
        }
        MatcherAssert.assertThat("Throw an NullPointerException of interface", Matchers.equalTo(interfaceThrown));
    }

}
