package com.github.bigwg.easy.spring.test.test.context.iface;

import com.github.bigwg.easy.spring.test.annotation.AutoImport;
import com.github.bigwg.easy.spring.test.test.BaseTest;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * 自动引入接口测试类
 *
 * @author zhaozhiwei
 * @since 2023/11/30
 */
@AutoImport(value = {HelloServiceI.class, AbstractQuackService.class}, scanBasePackages = "com.github.bigwg.easy.spring.test.test.context")
public class AutoImportInterfaceTest extends BaseTest {

    @Autowired
    @Qualifier("helloBoyServiceImpl")
    private HelloServiceI helloBoyService;

    @Autowired
    @Qualifier("helloWorldServiceImpl")
    private HelloServiceI helloWorldService;

    @Autowired
    private AbstractQuackService quackService;

    @Test
    public void autoImportHelloBoyInterfaceTest() {
        String hello = helloBoyService.hello();
        MatcherAssert.assertThat(hello, Matchers.equalTo("Hello Boy"));
    }

    @Test
    public void autoImportHelloWorldInterfaceTest() {
        String hello = helloWorldService.hello();
        MatcherAssert.assertThat(hello, Matchers.equalTo("Hello World"));
    }

    @Test
    public void autoImportAbstractTest() {
        String quack = quackService.quack();
        MatcherAssert.assertThat(quack, Matchers.equalTo("Quack World"));
    }

}
