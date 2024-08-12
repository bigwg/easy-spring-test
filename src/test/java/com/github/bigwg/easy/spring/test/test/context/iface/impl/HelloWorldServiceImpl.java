package com.github.bigwg.easy.spring.test.test.context.iface.impl;

import com.github.bigwg.easy.spring.test.test.context.iface.HelloServiceI;
import org.springframework.stereotype.Service;

/**
 * HelloWorldServiceImpl
 *
 * @author zhaozhiwei
 * @since 2023/11/30
 */
@Service
public class HelloWorldServiceImpl implements HelloServiceI {

    @Override
    public String hello() {
        return "Hello World";
    }

}
