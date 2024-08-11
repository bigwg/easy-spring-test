package com.github.bigwg.easy.spring.test.test.context.iface.impl;

import com.github.bigwg.easy.spring.test.test.context.iface.AbstractQuackService;
import org.springframework.stereotype.Service;

/**
 * HelloWorldServiceImpl
 *
 * @author zhaozhiwei
 * @since 2023/11/30
 */
@Service
public class QuackWorldServiceImpl extends AbstractQuackService {

    public String quack() {
        return "Quack World";
    }

}
