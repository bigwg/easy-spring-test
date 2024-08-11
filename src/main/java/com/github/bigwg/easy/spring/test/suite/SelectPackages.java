package com.github.bigwg.easy.spring.test.suite;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 测试套件扫包配置注解
 *
 * @author zhaozhiwei
 * @since 2022/3/3
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SelectPackages {

    String[] value() default {};

}
