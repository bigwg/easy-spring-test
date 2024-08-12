package com.github.bigwg.easy.spring.test.test.context.iface.impl;

import com.github.bigwg.easy.spring.test.test.context.iface.HelloServiceI;
import org.springframework.stereotype.Service;

/**
 * HelloBoyServiceImpl
 *
 * @author zhaozhiwei
 * @since 2023/11/30
 */
@Service
public class HelloBoyServiceImpl implements HelloServiceI {

    @Override
    public String hello() {
        return "Hello Boy";
    }

}
