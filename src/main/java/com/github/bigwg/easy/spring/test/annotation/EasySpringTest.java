package com.github.bigwg.easy.spring.test.annotation;

import com.github.bigwg.easy.spring.test.context.SmartSpringTestContextBootstrapper;
import com.github.bigwg.easy.spring.test.mock.MockitoStaticTestExecutionListener;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.boot.test.mock.mockito.ResetMocksTestExecutionListener;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextBeforeModesTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SmartSpringTest
 *
 * @author zhaozhiwei
 * @since 2022/3/12
 */
@AutoImport
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@TestExecutionListeners({
        MockitoTestExecutionListener.class, ResetMocksTestExecutionListener.class, DependencyInjectionTestExecutionListener.class,
        DirtiesContextBeforeModesTestExecutionListener.class, DirtiesContextTestExecutionListener.class, SqlScriptsTestExecutionListener.class,
        TransactionalTestExecutionListener.class, MockitoStaticTestExecutionListener.class
})
@BootstrapWith(SmartSpringTestContextBootstrapper.class)
public @interface EasySpringTest {
}
