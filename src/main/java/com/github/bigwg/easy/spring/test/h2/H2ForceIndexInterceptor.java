package com.github.bigwg.easy.spring.test.h2;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * H2ForceIndexInterceptor
 *
 * @author zhaozhiwei
 * @since 2022/4/6
 */
@Intercepts(@Signature(type = StatementHandler.class, method = "prepare", args = {
        Connection.class, Integer.class}))
public class H2ForceIndexInterceptor implements Interceptor {

    /**
     * force index pattern
     */
    private static final Pattern FORCE_INDEX_PATTERN = Pattern.compile("force\\s+index");

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler handler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = handler.getBoundSql();
        String sql = boundSql.getSql();
        sql = FORCE_INDEX_PATTERN.matcher(sql).replaceAll("use index");
        Field sqlField = boundSql.getClass().getDeclaredField("sql");
        sqlField.setAccessible(true);
        sqlField.set(boundSql, sql);
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

}
