package com.springboot.provider.common.proxy;

import com.springboot.provider.common.holder.ApplicationContextDataSourceHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.*;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;

public class JdbcOperationsProxy {
    private static final Logger logger = LoggerFactory.getLogger(JdbcOperationsProxy.class);
    private static final ConcurrentHashMap<String, JdbcOperations> JDBC_OPERATIONS_MAP = new ConcurrentHashMap<>();

    public static JdbcOperations getProxyInstance(String dsName) {
        if (JDBC_OPERATIONS_MAP.get(dsName) == null) {
            DataSource dataSource = ApplicationContextDataSourceHolder.getDataSource(dsName);
            Assert.notNull(dataSource, dsName + " datasource is not exists in MultiDataSourceHolder!");

            JDBC_OPERATIONS_MAP.putIfAbsent(dsName, getProxyInstance(dataSource));
        }
        return JDBC_OPERATIONS_MAP.get(dsName);
    }

    public static JdbcOperations getProxyInstance(DataSource dataSource) {
        Assert.notNull(dataSource, "DataSource must not be null");

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        return getInstance(jdbcTemplate);
    }


    private static JdbcOperations getInstance(JdbcTemplate jdbcTemplate) {
        Assert.notNull(jdbcTemplate, "JdbcTemplate must not be null");

        return (JdbcOperations) Proxy.newProxyInstance(JdbcOperations.class.getClassLoader(), new Class<?>[]{JdbcOperations.class}, (proxy, method, args) -> {
            String sql = preparedStatementFormatter(args);

            Object result = null;
            try {
                long l = System.currentTimeMillis();
                result = method.invoke(jdbcTemplate, args);

                logger.info("\nJdbcOperations Method: {} \nSQL: {} \nInvoke Cost: {}", method.getName(), sql, (System.currentTimeMillis() - l) + "ms");
            } catch (Exception e) {
                logger.error("\nSQL: {} \nError Message: {}", sql, e.getCause().toString());
            }

            return result;
        });
    }

    private static String preparedStatementFormatter(Object[] args) {
        AtomicReference<String> sql = new AtomicReference<>("");

        // implant the args to sql
        Arrays.stream(args).forEach(item -> {
            if (item instanceof String) {
                sql.set((String) item);
            } else if (item instanceof Object[]) {
                Arrays.stream(((Object[]) item)).forEach(data -> {
                    String value = Matcher.quoteReplacement(String.valueOf(data));
                    sql.updateAndGet(s -> s.replaceFirst("\\?", StringUtils.center(value, value.length() + 2, "'")));
                });
            } else if (item instanceof PreparedStatementCreator && item instanceof PreparedStatementSetter
                    && item instanceof SqlProvider && item instanceof ParameterDisposer) {

                sql.set(((SqlProvider) item).getSql());
            }
        });

        return sql.accumulateAndGet(";", (s, s2) -> s + s2);
    }

}
