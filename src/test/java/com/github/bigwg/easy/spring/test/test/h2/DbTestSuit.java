package com.github.bigwg.easy.spring.test.test.h2;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * DB测试套件
 *
 * @author zhaozhiwei
 * @since 2023/4/25
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({DbReload1Test.class, DbReload2Test.class})
public class DbTestSuit {
}
