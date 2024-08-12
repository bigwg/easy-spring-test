package com.github.bigwg.easy.spring.test.test.suite;

import com.github.bigwg.easy.spring.test.suite.EasySuite;
import com.github.bigwg.easy.spring.test.suite.IncludeClass;
import com.github.bigwg.easy.spring.test.suite.SelectPackages;
import com.github.bigwg.easy.spring.test.test.mock.simple.MockVerifyTest;
import com.github.bigwg.easy.spring.test.test.mock.simple.ProgrammingMockTest;
import org.junit.runner.RunWith;

/**
 * IncludeClass 注解测试
 *
 * @author zhaozhiwei
 * @since 2023/4/25
 */
@RunWith(EasySuite.class)
@SelectPackages("com.github.bigwg.easy.spring.test.test.mock.spring")
@IncludeClass({MockVerifyTest.class, ProgrammingMockTest.class})
public class IncludeClassTest {
}
