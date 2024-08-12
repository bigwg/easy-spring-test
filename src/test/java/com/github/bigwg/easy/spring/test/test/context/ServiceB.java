package com.github.bigwg.easy.spring.test.test.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ServiceB
 *
 * @author zhaozhiwei
 * @since 2023/4/25
 */
@Service
public class ServiceB {

    @Autowired
    private ServiceC serviceC;

    public String hello() {
        return "ServiceB";
    }

}
