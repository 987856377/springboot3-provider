package com.springboot.provider.common.holder;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;

/**
 * @Description 单数据源控制器
 * @Project development
 * @Package com.spring.development.datasource
 * @Author xuzhenkui
 * @Date 2020/2/26 18:27
 */
public class DataSourceHolder {
    private static final ThreadLocal<DataSource> DATA_SOURCE_THREAD_LOCAL = new ThreadLocal<>();

    static {
        DATA_SOURCE_THREAD_LOCAL.set(DataSourceBuilder.create()
                .type(MysqlDataSource.class)
                .url("jdbc:mysql://localhost:3306/development?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&serverTimezone=Asia/Shanghai&useSSL=false")
                .username("root")
                .password("root").build());
    }

    public static DataSource getDataSource() {
        return DATA_SOURCE_THREAD_LOCAL.get();
    }

    public static void setDataSource(DataSource dataSource) {
        DATA_SOURCE_THREAD_LOCAL.set(dataSource);
    }
}
