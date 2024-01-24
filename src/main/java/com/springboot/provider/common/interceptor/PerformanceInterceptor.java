package com.springboot.provider.common.interceptor;

import cn.hutool.core.lang.id.NanoId;
import cn.hutool.db.sql.SqlFormatter;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.core.toolkit.SystemClock;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.*;

/**
 * @Description 性能分析拦截器，用于输出每条 SQL 语句及其执行时间
 * @Project springboot-provider
 * @Package com.springboot.provider.common.interceptor
 * @Author xuzhenkui
 * @Date 2021/11/28 16:50
 */
@Component
@Intercepts({@Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
        @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})})
public class PerformanceInterceptor implements Interceptor {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * SQL 执行最大时长，超过自动停止运行，有助于发现问题。
     */
    private long maxTime = 0;
    /**
     * SQL 是否格式化
     */
    private boolean format = false;
    /**
     * 是否写入日志文件<br>
     * true 写入日志文件，不阻断程序执行！<br>
     * 超过设定的最大执行时长异常提示！
     */
    private boolean writeInLog = false;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final DataSource dataSource;

    public PerformanceInterceptor(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());

        PluginUtils.MPStatementHandler mpStatementHandler = PluginUtils.mpStatementHandler(statementHandler);

        String originalSql = mpStatementHandler.mPBoundSql().sql();
        Object parameterObject = mpStatementHandler.mPBoundSql().parameterObject();

        originalSql = originalSql.replaceAll("[\\s]+", " ");
        int index = indexOfSqlStart(originalSql);
        if (index > 0) {
            originalSql = originalSql.substring(index);
        }

        // 格式化 SQL 打印执行结果
        Map<String, Object> printMap = new HashMap<>();
        if (parameterObject instanceof Map) {
            HashMap<String, Object> paramsMap = (HashMap) parameterObject;
            if (!ObjectUtils.isEmpty(paramsMap)) {
                paramsMap.keySet().forEach(key -> {
                    if (!key.startsWith("param")) {
                        printMap.put(key, paramsMap.get(key));
                    }
                });
            }
        }

        String invokeId = NanoId.randomNanoId();

        // 格式化 SQL 打印执行结果
        String formatSql = " >>> Datasource：" + dataSource.toString() +
                "\n - invokeId：" + invokeId +
                "\n - ID：" + mpStatementHandler.mappedStatement().getId() +
                "\n - SQL：" + sqlFormat(originalSql, true) +
                "\n - Parameter：" + objectMapper.writeValueAsString(printMap.isEmpty() ? parameterObject : printMap);

        // 计算执行 SQL 耗时
        long start = SystemClock.now();
        Object result = invocation.proceed();
        long timing = SystemClock.now() - start;

        // 打印SQL执行时间
        String invokeCost = " >>> Datasource：" + dataSource.toString() +
                "\n - invokeId：" + invokeId +
                "\n - ID：" + mpStatementHandler.mappedStatement().getId() +
                "\n - Cost：" + timing + " ms";

        if (this.isWriteInLog()) {
            if (this.getMaxTime() >= 1 && timing > this.getMaxTime()) {
                logger.error(formatSql.toString());
                logger.error(invokeCost.toString());
            } else {
                logger.info(formatSql.toString());
                logger.info(invokeCost.toString());
            }
        } else {
            logger.info(formatSql.toString());
            logger.info(invokeCost.toString());
            if (this.getMaxTime() >= 1 && timing > this.getMaxTime()) {
                throw new RuntimeException(" The SQL execution time is too large, please optimize ! ");
            }
        }
        return result;
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties prop) {
        String maxTime = prop.getProperty("maxTime");
        String format = prop.getProperty("format");
        if (StringUtils.isNotEmpty(maxTime)) {
            this.maxTime = Long.parseLong(maxTime);
        }
        if (StringUtils.isNotEmpty(format)) {
            this.format = Boolean.parseBoolean(format);
        }
    }

    public long getMaxTime() {
        return maxTime;
    }

    public PerformanceInterceptor setMaxTime(long maxTime) {
        this.maxTime = maxTime;
        return this;
    }

    public boolean isFormat() {
        return format;
    }

    public PerformanceInterceptor setFormat(boolean format) {
        this.format = format;
        return this;
    }

    public boolean isWriteInLog() {
        return writeInLog;
    }

    public PerformanceInterceptor setWriteInLog(boolean writeInLog) {
        this.writeInLog = writeInLog;
        return this;
    }

    public Method getMethodRegular(Class<?> clazz, String methodName) {
        if (Object.class.equals(clazz)) {
            return null;
        }
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return getMethodRegular(clazz.getSuperclass(), methodName);
    }

    /**
     * 获取sql语句开头部分
     *
     * @param sql
     * @return
     */
    private int indexOfSqlStart(String sql) {
        String upperCaseSql = sql.toUpperCase();
        Set<Integer> set = new HashSet<>();
        set.add(upperCaseSql.indexOf("SELECT "));
        set.add(upperCaseSql.indexOf("UPDATE "));
        set.add(upperCaseSql.indexOf("INSERT "));
        set.add(upperCaseSql.indexOf("DELETE "));
        set.remove(-1);
        if (CollectionUtils.isEmpty(set)) {
            return -1;
        }
        List<Integer> list = new ArrayList<>(set);
        Collections.sort(list, Integer::compareTo);
        return list.get(0);
    }


    /**
     * 格式sql
     *
     * @param boundSql
     * @param format
     * @return
     */
    public static String sqlFormat(String boundSql, boolean format) {
        if (format) {
            try {
                return SqlFormatter.format(boundSql);
            } catch (Exception ignored) {
            }
        }
        return boundSql;
    }
}
