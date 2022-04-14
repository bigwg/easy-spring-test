package com.github.bigwg.easy.spring.test.test.context;

import org.junit.jupiter.api.Test;

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
    public static void main(String[] args) {
        String sql = "select * from aaa force     index(idx_date_mod)"+
                "where id > #{id}"+
                "and user_id_mod = #{userIdMod} "+
                "and target_date = #{targetDate}"+
                "and valid = 1"+
                "order by id limit #{pageSize}";
        String use_index = FORCE_INDEX_PATTERN.matcher(sql).replaceAll("use index");
        System.out.println(use_index);

    }

}
