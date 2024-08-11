package com.github.bigwg.easy.spring.test.annotation;

import org.mockito.Answers;
import org.springframework.boot.test.mock.mockito.MockReset;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 静态类mock
 *
 * @author zhaozhiwei
 * @since 2022/3/3
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MockStatic {

    Class<?> value();

    Answers answers() default Answers.RETURNS_DEFAULTS;

    MockReset reset() default MockReset.AFTER;

}
