package com.springboot.provider.common.holder;

import com.springboot.provider.common.builder.HikariDataSourceBuilder;
import com.springboot.provider.common.enums.DataSourceEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
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
public class ApplicationContextDataSourceHolder {

    private static final ConcurrentHashMap<String, DataSource> DATA_SOURCE_MAP = new ConcurrentHashMap<>();

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
    public static Boolean addDataSource(String dsName, DataSource dataSource) {
        if (StringUtils.hasText(dsName) && dataSource != null) {
//        如果传入key对应的value已经存在，就返回存在的value，不进行替换。如果不存在，就添加key和value，返回null
            return DATA_SOURCE_MAP.putIfAbsent(dsName, dataSource) == null;
        }
        return false;
    }

    /**
     * 删除数据源
     *
     * @param dsName 数据源名称
     */
    public static Boolean removeDataSource(String dsName) {
        if (StringUtils.hasText(dsName)) {
            DATA_SOURCE_MAP.remove(dsName);
            return true;
        }
        return false;
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

    /**
     * 创建 Hikari 构造器
     *
     * @return HikariDataSourceBuilder
     */
    public static HikariDataSourceBuilder builder() {
        return HikariDataSourceBuilder.create();
    }
}
