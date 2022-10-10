package com.springboot.provider.common.enums;

import com.mysql.cj.jdbc.MysqlDataSource;
import oracle.jdbc.pool.OracleDataSource;

/**
 * @program: springboot-dev
 * @package com.spring.development.common.enums
 * @description
 * @author: XuZhenkui
 * @create: 2020-12-02 16:15
 **/
public enum DataSourceEnum {

    MYSQL("MYSQL", MysqlDataSource.class, "jdbc:mysql://",
            "?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&serverTimezone=Asia/Shanghai&useSSL=false"),

    ORACLE("ORACLE", OracleDataSource.class, "jdbc:oracle:thin:@", "");

    private final String dbType;
    private final Class dataSourceType;
    private final String prefix;
    private final String suffix;

    DataSourceEnum(String dbType, Class dataSourceType, String prefix, String suffix) {
        this.dbType = dbType;
        this.dataSourceType = dataSourceType;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public String getDbType() {
        return dbType;
    }

    public Class getDataSourceType() {
        return dataSourceType;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }
}
