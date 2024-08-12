package com.github.bigwg.easy.spring.test.test.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ServiceA
 *
 * @author zhaozhiwei
 * @since 2023/4/25
 */
@Service
public class ServiceA {

    @Autowired
    private ServiceB serviceB;

    @Autowired
    private ServiceC serviceC;

    public String hello() {
        return "ServiceA";
    }

    public String invokeServiceB() {
        return "invoke ServiceB: " + serviceB.hello();
    }

    public String invokeServiceC() {
        return "invoke ServiceC: " + serviceC.hello();
    }

}
