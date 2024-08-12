package com.github.bigwg.easy.spring.test.test.context.simple;

import org.springframework.stereotype.Service;

/**
 * SimpleServiceA
 *
 * @author zhaozhiwei
 * @since 2023/4/25
 */
@Service("simpleServiceA")
public class SimpleServiceA {

    public String hello() {
        return "SimpleServiceA";
    }

}
