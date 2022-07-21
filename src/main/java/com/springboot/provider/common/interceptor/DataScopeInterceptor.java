package com.springboot.provider.common.interceptor;

import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;

/**
 * @Description
 * @Project springboot-provider
 * @Package com.springboot.provider.config
 * @Author xuzhenkui
 * @Date 2021/8/3 10:07
 */
@Component
@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class}),
        @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
})
public class DataScopeInterceptor implements Interceptor {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final DataSource dataSource;

    public DataScopeInterceptor(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object target = invocation.getTarget();
        Object[] args = invocation.getArgs();

        if (target instanceof Executor) {
            logger.info("Current DataSource: {}", dataSource.toString());
            final Executor executor = (Executor) target;
            Object parameter = args[1];
            boolean isUpdate = args.length == 2;
            MappedStatement ms = (MappedStatement) args[0];
            if (!isUpdate && ms.getSqlCommandType() == SqlCommandType.SELECT) {
                RowBounds rowBounds = (RowBounds) args[2];
                ResultHandler resultHandler = (ResultHandler) args[3];
                BoundSql boundSql;
                if (args.length == 4) {
                    boundSql = ms.getBoundSql(parameter);
                } else {
                    // 几乎不可能走进这里面,除非使用Executor的代理对象调用query[args[6]]
                    boundSql = (BoundSql) args[5];
                }
                CacheKey cacheKey = executor.createCacheKey(ms, parameter, rowBounds, boundSql);
                return executor.query(ms, parameter, rowBounds, resultHandler, cacheKey, boundSql);
            }
        } else {
            StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());

            PluginUtils.MPStatementHandler mpStatementHandler = PluginUtils.mpStatementHandler(statementHandler);

            if (!SqlCommandType.SELECT.equals(mpStatementHandler.mappedStatement().getSqlCommandType())) {
                return invocation.proceed();
            }

            String originalSql = mpStatementHandler.mPBoundSql().sql();
            Object parameterObject = mpStatementHandler.mPBoundSql().parameterObject();

            return invocation.proceed();
        }
        return invocation.proceed();
    }

    /**
     * 生成拦截对象的代理
     *
     * @param target 目标对象
     * @return 代理对象
     */
    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor || target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    /**
     * mybatis配置的属性
     *
     * @param properties mybatis配置的属性
     */
    @Override
    public void setProperties(Properties properties) {

    }

}
