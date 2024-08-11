package com.github.bigwg.easy.spring.test.test.context.multiple;

import org.springframework.stereotype.Service;

/**
 * MultipleServiceA
 *
 * @author zhaozhiwei
 * @since 2023/4/26
 */
@Service("multipleServiceA")
public class MultipleServiceA implements MultipleBeanServiceI {

    @Override
    public String hello() {
        return "MultipleServiceA";
    }

}
