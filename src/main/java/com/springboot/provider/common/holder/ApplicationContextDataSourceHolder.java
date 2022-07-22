package com.springboot.provider.common.holder;

import com.springboot.provider.common.builder.HikariDataSourceBuilder;
import com.springboot.provider.common.enums.DataSourceEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description 多数据源控制器
 * @Project development
 * @Package com.spring.development.datasource
 * @Author xuzhenkui
 * @Date 2020/2/26 18:27
 */
@Component
public class ApplicationContextDataSourceHolder implements InitializingBean, DisposableBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationContextDataSourceHolder.class);

    private static final ConcurrentHashMap<String, DataSource> DATA_SOURCE_MAP = new ConcurrentHashMap<>();

    private final ApplicationContext applicationContext;

    public ApplicationContextDataSourceHolder(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.info("ApplicationContextDataSourceHandler start loading datasource ....");

        String[] beanNamesForType = this.applicationContext.getBeanNamesForType(DataSource.class);
        for (String beanName : beanNamesForType) {
            Object bean = this.applicationContext.getBean(beanName);
            ApplicationContextDataSourceHolder.addDataSource(beanName, (DataSource) bean);
        }

        LOGGER.info("ApplicationContextDataSourceHandler load context datasource: " + Arrays.toString(beanNamesForType));
    }

    public static ConcurrentHashMap<String, DataSource> getDataSourceMap() {
        return DATA_SOURCE_MAP;
    }

    /**
     * 根据数据源名称获取数据源
     *
     * @param dsName 数据源名称
     * @return
     */
    public static DataSource getDataSource(String dsName) {
        if (StringUtils.hasText(dsName)) {
            return DATA_SOURCE_MAP.get(dsName);
        }
        return null;
    }

    /**
     * 添加数据源
     *
     * @param dsName     数据源名称
     * @param dataSource 数据源
     */
    public static synchronized void addDataSource(String dsName, DataSource dataSource) {
        DataSource oldDataSource = DATA_SOURCE_MAP.put(dsName, dataSource);
        // 关闭老的数据源
        if (oldDataSource != null) {
            closeDataSource(oldDataSource);
            LOGGER.info("ApplicationContextDataSourceHolder close old datasource named [{}] success", dsName);
        }
        LOGGER.info("ApplicationContextDataSourceHolder - add datasource named [{}] success", dsName);
    }

    /**
     * 删除数据源
     *
     * @param dsName 数据源名称
     */
    public static void removeDataSource(String dsName) {
        if (!StringUtils.hasText(dsName)) {
            throw new RuntimeException("remove parameter could not be empty");
        }
        if (DATA_SOURCE_MAP.containsKey(dsName)) {
            DataSource dataSource = DATA_SOURCE_MAP.remove(dsName);
            closeDataSource(dataSource);
            LOGGER.info("ApplicationContextDataSourceHolder - remove the database named [{}] success", dsName);
        } else {
            LOGGER.warn("ApplicationContextDataSourceHolder - could not find a database named [{}]", dsName);
        }
    }

    private static void closeDataSource(DataSource dataSource) {
        try {
            Method closeMethod = ReflectionUtils.findMethod(dataSource.getClass(), "close");
            if (closeMethod != null) {
                closeMethod.invoke(dataSource);
                LOGGER.info("ApplicationContextDataSourceHolder close datasource named [{}] success", dataSource);
            } else {
                closeMethod = ReflectionUtils.findMethod(dataSource.getClass(), "destroy");
                if (closeMethod != null) {
                    closeMethod.invoke(dataSource);
                    LOGGER.info("ApplicationContextDataSourceHolder destroy datasource named [{}] success", dataSource);
                } else {
                    LOGGER.warn("ApplicationContextDataSourceHolder close or destroy datasource named [{}] failed", dataSource);
                }
            }
        } catch (Exception e) {
            LOGGER.warn("ApplicationContextDataSourceHolder closeDataSource named [{}] failed", dataSource, e);
        }
    }

    /**
     * 创建 Hikari 构造器
     *
     * @return HikariDataSourceBuilder
     */
    public static HikariDataSourceBuilder builder() {
        return HikariDataSourceBuilder.create();
    }

    /**
     * 构建数据源
     *
     * @param dbType   数据库类型
     * @param ip       数据库地址
     * @param port     数据库端口
     * @param instance 数据库
     * @param username 用户名
     * @param password 密码
     * @param etc      预留尾部参数配置
     * @return
     */
    public static DataSource buildDataSource(String dbType, String ip, String port, String instance, String username, String password, String etc) {
        dbType = dbType.toUpperCase();

        Class dataSourceType;
        String url;

        if (dbType.equals(DataSourceEnum.MYSQL.getDbType())) {
            dataSourceType = DataSourceEnum.MYSQL.getDataSourceType();
            url = DataSourceEnum.MYSQL.getPrefix() + ip + ":" + port + "/" + instance + DataSourceEnum.MYSQL.getSuffix() + etc;
        } else if (dbType.equals(DataSourceEnum.ORACLE.getDbType())) {
            dataSourceType = DataSourceEnum.ORACLE.getDataSourceType();
            url = DataSourceEnum.ORACLE.getPrefix() + ip + ":" + port + ":" + instance + DataSourceEnum.ORACLE.getSuffix() + etc;
        } else {
            return null;
        }

        return DataSourceBuilder.create()
                .type(dataSourceType)
                .url(url)
                .username(username)
                .password(password).build();
    }

    @Override
    public void destroy() throws Exception {
        LOGGER.info("ApplicationContextDataSourceHolder start closing ....");

        DATA_SOURCE_MAP.values().forEach(ApplicationContextDataSourceHolder::closeDataSource);

        LOGGER.info("ApplicationContextDataSourceHolder all closed success,bye");
    }

}
