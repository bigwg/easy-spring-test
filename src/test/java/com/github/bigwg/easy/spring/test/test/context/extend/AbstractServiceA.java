package com.github.bigwg.easy.spring.test.test.context.extend;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * AbstractServiceA
 *
 * @author zhaozhiwei
 * @since 2023/4/26
 */
public abstract class AbstractServiceA {

    @Autowired
    public ExtendServiceA extendServiceA;

}
