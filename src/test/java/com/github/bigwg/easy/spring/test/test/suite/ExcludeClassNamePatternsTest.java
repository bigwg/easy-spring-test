package com.github.bigwg.easy.spring.test.test.suite;

import com.github.bigwg.easy.spring.test.suite.EasySuite;
import com.github.bigwg.easy.spring.test.suite.ExcludeClassNamePatterns;
import com.github.bigwg.easy.spring.test.suite.SelectPackages;
import org.junit.runner.RunWith;

/**
 * ExcludeClassNamePatterns 注解测试
 *
 * @author zhaozhiwei
 * @since 2023/4/25
 */
@RunWith(EasySuite.class)
@SelectPackages("com.github.bigwg.easy.spring.test.test.rhino")
@ExcludeClassNamePatterns({".*BreakerServiceTest"})
public class ExcludeClassNamePatternsTest {
}
