package com.github.bigwg.easy.spring.test.test.context.configuration;

import com.github.bigwg.easy.spring.test.annotation.AutoImport;
import com.github.bigwg.easy.spring.test.test.BaseTest;
import com.github.bigwg.easy.spring.test.test.context.ApplicationContextUtil;
import com.github.bigwg.easy.spring.test.test.context.ServiceA;
import com.github.bigwg.easy.spring.test.util.BeanDefinitionRegistryUtil;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.beans.PropertyDescriptor;

/**
 * CreateBeanConfigTest
 *
 * @author zhaozhiwei
 * @since 2024/6/8
 */
@AutoImport({CreateBeanConfig.class, ApplicationContextUtil.class})
public class CreateBeanConfigTest extends BaseTest {

    @Autowired
    private ServiceA serviceA;
    @Autowired
    public CreateBeanConfig config;

    @Test
    public void testConfigBeanImport(){
        ServiceA serviceA1 = ApplicationContextUtil.popBean(ServiceA.class);
        MatcherAssert.assertThat(serviceA, Matchers.notNullValue());
        String expectInvokeRes = "invoke ServiceB: ServiceB";
        String invoke = serviceA.invokeServiceB();
        MatcherAssert.assertThat(invoke, Matchers.notNullValue());
        MatcherAssert.assertThat(invoke, Matchers.equalTo(expectInvokeRes));
        PropertyDescriptor config1 = BeanUtils.getPropertyDescriptor(CreateBeanConfigTest.class, "config");
    }
}
