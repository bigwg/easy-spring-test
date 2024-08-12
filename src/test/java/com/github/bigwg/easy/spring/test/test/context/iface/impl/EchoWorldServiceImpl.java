package com.github.bigwg.easy.spring.test.test.context.iface.impl;

import com.github.bigwg.easy.spring.test.test.context.iface.EchoServiceI;
import org.springframework.stereotype.Service;

/**
 * HelloWorldServiceImpl
 *
 * @author zhaozhiwei
 * @since 2023/11/30
 */
@Service
public class EchoWorldServiceImpl implements EchoServiceI {

    @Override
    public String echo() {
        return "Echo World";
    }

}
