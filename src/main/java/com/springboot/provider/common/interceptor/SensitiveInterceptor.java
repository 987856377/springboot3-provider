package com.springboot.provider.common.interceptor;

import cn.hutool.core.lang.id.NanoId;
import com.springboot.provider.module.his.entity.User;
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
import java.util.Collection;
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
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
})
public class SensitiveInterceptor implements Interceptor {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final DataSource dataSource;

    public SensitiveInterceptor(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object target = invocation.getTarget();
        Object[] args = invocation.getArgs();

        if (target instanceof Executor) {
            logger.info("Current DataSource: {}", dataSource.toString());
            MappedStatement ms = (MappedStatement) args[0];
            if (ms.getSqlCommandType() == SqlCommandType.SELECT) {
                Object proceed = invocation.proceed();
                if (proceed instanceof Collection) {
                    Collection<Object> collection = (Collection<Object>) proceed;
                    if (!collection.isEmpty() && collection.stream().findFirst().get() instanceof User) {
                        collection.forEach(item -> {
                            User user = (User) item;
                            user.setUsername("decrypt" + NanoId.randomNanoId());
                        });
                        return collection;
                    }
                }
                return proceed;
            } else if (ms.getSqlCommandType() == SqlCommandType.INSERT || ms.getSqlCommandType() == SqlCommandType.UPDATE) {
                if (args[1] instanceof User) {
                    User user = (User) args[1];
                    user.setUsername("encrypt" + NanoId.randomNanoId());
                }
                return invocation.proceed();
            }
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
