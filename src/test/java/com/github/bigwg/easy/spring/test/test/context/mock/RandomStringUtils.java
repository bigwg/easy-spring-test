package com.github.bigwg.easy.spring.test.test.context.mock;

import java.util.UUID;

/**
 * RandomStringUtils
 *
 * @author zhaozhiwei
 * @since 2024/3/4
 */
public class RandomStringUtils {

    public static String random() {
        return UUID.randomUUID().toString();
    }

}
