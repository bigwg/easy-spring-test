package com.github.bigwg.easy.spring.test.test.context.abstraction;

import com.github.bigwg.easy.spring.test.test.context.ServiceC;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * AbstractService
 *
 * @author zhaozhiwei
 * @since 2023/4/25
 */
public abstract class AbstractService {

    @Autowired
    protected ServiceC serviceC;

}
