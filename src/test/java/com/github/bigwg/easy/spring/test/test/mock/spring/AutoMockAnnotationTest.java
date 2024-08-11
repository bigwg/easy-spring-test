package com.github.bigwg.easy.spring.test.test.mock.spring;

import com.github.bigwg.easy.spring.test.annotation.AutoMock;
import com.github.bigwg.easy.spring.test.test.BaseTest;
import com.github.bigwg.easy.spring.test.test.mock.spring.auto.mock.wildcard.one.AutoMockServiceA;
import com.github.bigwg.easy.spring.test.test.mock.spring.auto.mock.wildcard.one.AutoMockServiceB;
import com.github.bigwg.easy.spring.test.test.mock.spring.auto.mock.wildcard.one.child.AutoMockServiceE;
import com.github.bigwg.easy.spring.test.test.mock.spring.auto.mock.wildcard.two.AutoMockServiceC;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * AutoMockAnnotationTest
 *
 * @author zhaozhiwei
 * @since 2022/3/3
 */
@AutoMock(value = {AutoMockClassServiceA.class, AutoMockClassServiceB.class},
        scanBasePackages = {"com.github.bigwg.easy.spring.test.test.mock.spring.auto.mock.wildcard.one.*",
                "com.github.bigwg.easy.spring.test.test.mock.spring.auto.mock.wildcard.two"})
public class AutoMockAnnotationTest extends BaseTest implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    private AutoMockPathService autoMockPathService;
    @Autowired
    private AutoMockClassService autoMockClassService;


    @Test
    public void testAutoMockImportClass() {
        boolean invokeMock = autoMockClassService.invokeMock();
        MatcherAssert.assertThat(invokeMock, Matchers.equalTo(true));
    }

    @Test
    public void testAutoMockImportPath() {
        boolean invokeMock = autoMockPathService.invokeMock();
        MatcherAssert.assertThat(invokeMock, Matchers.equalTo(true));
    }

    @Test
    public void testAllAutoMock() {
        AutoMockServiceA autoMockServiceA = applicationContext.getBean(AutoMockServiceA.class);
        AutoMockServiceB autoMockServiceB = applicationContext.getBean(AutoMockServiceB.class);
        AutoMockServiceC autoMockServiceC = applicationContext.getBean(AutoMockServiceC.class);
        AutoMockServiceE autoMockServiceE = applicationContext.getBean(AutoMockServiceE.class);
        MatcherAssert.assertThat(autoMockServiceA, Matchers.notNullValue());
        MatcherAssert.assertThat((Mockito.mockingDetails(autoMockServiceA).isMock()
                || Mockito.mockingDetails(autoMockServiceA).isSpy()), Matchers.equalTo(true));

        MatcherAssert.assertThat(autoMockServiceB, Matchers.notNullValue());
        MatcherAssert.assertThat((Mockito.mockingDetails(autoMockServiceB).isMock()
                || Mockito.mockingDetails(autoMockServiceB).isSpy()), Matchers.equalTo(true));

        MatcherAssert.assertThat(autoMockServiceC, Matchers.notNullValue());
        MatcherAssert.assertThat((Mockito.mockingDetails(autoMockServiceC).isMock()
                || Mockito.mockingDetails(autoMockServiceC).isSpy()), Matchers.equalTo(true));

        MatcherAssert.assertThat(autoMockServiceE, Matchers.notNullValue());
        MatcherAssert.assertThat((Mockito.mockingDetails(autoMockServiceE).isMock()
                || Mockito.mockingDetails(autoMockServiceE).isSpy()), Matchers.equalTo(true));

        AutoMockServiceD autoMockServiceD = null;
        try {
            autoMockServiceD = applicationContext.getBean(AutoMockServiceD.class);
        } catch (Exception e) {
            MatcherAssert.assertThat(e, Matchers.instanceOf(NoSuchBeanDefinitionException.class));
        }
        MatcherAssert.assertThat(autoMockServiceD, Matchers.nullValue());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
