package com.github.bigwg.easy.spring.test.suite;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 需要包含的类名模式
 *
 * @author zhaozhiwei
 * @since 2024/6/16
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IncludeClassNamePatterns {

    String[] value() default {};

}
