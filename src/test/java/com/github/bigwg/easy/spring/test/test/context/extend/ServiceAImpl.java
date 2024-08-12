package com.github.bigwg.easy.spring.test.test.context.extend;

import org.springframework.stereotype.Service;

/**
 * ServiceA
 *
 * @author zhaozhiwei
 * @since 2023/4/25
 */
@Service
public class ServiceAImpl extends AbstractServiceA {

    public String invokeExtendServiceA() {
        return "invoke ExtendServiceA: " + extendServiceA.hello();
    }

}
