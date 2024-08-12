package com.github.bigwg.easy.spring.test.test.h2;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.regex.Pattern;

/**
 * H2ForceIndexInterceptorTest
 *
 * @author zhaozhiwei
 * @since 2022/4/6
 */
public class H2ForceIndexInterceptorTest {

    private static final Pattern FORCE_INDEX_PATTERN = Pattern.compile("force\\s+index");

    @Test
    public void forceIndexReplaceTest() {
        String sql = "select * from aaa force     index(idx_date_mod)" +
                "where id > #{id}" +
                "and user_id_mod = #{userIdMod} " +
                "and target_date = #{targetDate}" +
                "and valid = 1" +
                "order by id limit #{pageSize}";
        String replacedSql = FORCE_INDEX_PATTERN.matcher(sql).replaceAll("use index");
        MatcherAssert.assertThat(replacedSql, Matchers.not(Matchers.matchesPattern("force\\s+index")));
        MatcherAssert.assertThat(replacedSql, Matchers.containsString("use index"));
    }

}
