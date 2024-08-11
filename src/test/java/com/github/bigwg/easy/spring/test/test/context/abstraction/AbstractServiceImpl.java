package com.github.bigwg.easy.spring.test.test.context.abstraction;

import org.springframework.stereotype.Service;

/**
 * AbstractServiceImpl
 *
 * @author zhaozhiwei
 * @since 2023/9/8
 */
@Service
public class AbstractServiceImpl extends AbstractService {

    public String hello() {
        return serviceC.hello();
    }

}
