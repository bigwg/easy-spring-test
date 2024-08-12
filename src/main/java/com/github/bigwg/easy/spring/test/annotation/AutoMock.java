package com.github.bigwg.easy.spring.test.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动Mock
 *
 * @author zhaozhiwei
 * @since 2022/3/3
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoMock {

    Class<?>[] value() default {};

    String[] scanBasePackages() default {};

}