package com.springboot.provider.common.builder;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @Description Hikari数据源构造器
 * @Project development
 * @Package com.springboot.provider.common.builder
 * @Author xuzhenkui
 * @Date 2021/5/8 18:27
 */
public class HikariDataSourceBuilder {
    private Properties properties;

    private HikariDataSourceBuilder() {
        this.properties = new Properties();
    }

    public static HikariDataSourceBuilder create() {
        return new HikariDataSourceBuilder();
    }

    public HikariDataSourceBuilder catalog(String catalog) {
        this.properties.put("catalog", catalog);
        return this;
    }

    public HikariDataSourceBuilder jdbcUrl(String jdbcUrl) {
        this.properties.put("jdbcUrl", jdbcUrl);
        return this;
    }

    public HikariDataSourceBuilder driverClassName(String driverClassName) {
        this.properties.put("driverClassName", driverClassName);
        return this;
    }

    public HikariDataSourceBuilder username(String username) {
        this.properties.put("username", username);
        return this;
    }

    public HikariDataSourceBuilder password(String password) {
        this.properties.put("password", password);
        return this;
    }

    public HikariDataSourceBuilder maxPoolSize(String maxPoolSize) {
        this.properties.put("maxPoolSize", maxPoolSize);
        return this;
    }

    public HikariDataSourceBuilder maxLifetime(String maxLifetime) {
        this.properties.put("maxLifetime", maxLifetime);
        return this;
    }

    public HikariDataSourceBuilder minIdle(String minIdle) {
        this.properties.put("minIdle", minIdle);
        return this;
    }

    public HikariDataSourceBuilder idleTimeout(String idleTimeout) {
        this.properties.put("idleTimeout", idleTimeout);
        return this;
    }

    public HikariDataSourceBuilder connectionTimeout(String connectionTimeout) {
        this.properties.put("connectionTimeout", connectionTimeout);
        return this;
    }

    public HikariDataSourceBuilder validationTimeout(String validationTimeout) {
        this.properties.put("validationTimeout", validationTimeout);
        return this;
    }

    public HikariDataSourceBuilder leakDetectionThreshold(String leakDetectionThreshold) {
        this.properties.put("leakDetectionThreshold", leakDetectionThreshold);
        return this;
    }

    public HikariDataSourceBuilder initializationFailTimeout(String initializationFailTimeout) {
        this.properties.put("initializationFailTimeout", initializationFailTimeout);
        return this;
    }

    public HikariDataSourceBuilder connectionInitSql(String connectionInitSql) {
        this.properties.put("connectionInitSql", connectionInitSql);
        return this;
    }

    public HikariDataSourceBuilder connectionTestQuery(String connectionTestQuery) {
        this.properties.put("connectionTestQuery", connectionTestQuery);
        return this;
    }

    public HikariDataSourceBuilder dataSourceClassName(String dataSourceClassName) {
        this.properties.put("dataSourceClassName", dataSourceClassName);
        return this;
    }

    public HikariDataSourceBuilder dataSourceJndiName(String dataSourceJndiName) {
        this.properties.put("dataSourceJndiName", dataSourceJndiName);
        return this;
    }

    public HikariDataSourceBuilder exceptionOverrideClassName(String exceptionOverrideClassName) {
        this.properties.put("exceptionOverrideClassName", exceptionOverrideClassName);
        return this;
    }

    public HikariDataSourceBuilder poolName(String poolName) {
        this.properties.put("poolName", poolName);
        return this;
    }

    public HikariDataSourceBuilder schema(String schema) {
        this.properties.put("schema", schema);
        return this;
    }

    public HikariDataSourceBuilder transactionIsolationName(String transactionIsolationName) {
        this.properties.put("transactionIsolationName", transactionIsolationName);
        return this;
    }

    public HikariDataSourceBuilder isAutoCommit(String isAutoCommit) {
        this.properties.put("isAutoCommit", isAutoCommit);
        return this;
    }

    public HikariDataSourceBuilder isReadOnly(String isReadOnly) {
        this.properties.put("isReadOnly", isReadOnly);
        return this;
    }

    public HikariDataSourceBuilder isIsolateInternalQueries(String isIsolateInternalQueries) {
        this.properties.put("isIsolateInternalQueries", isIsolateInternalQueries);
        return this;
    }

    public HikariDataSourceBuilder isRegisterMbeans(String isRegisterMbeans) {
        this.properties.put("isRegisterMbeans", isRegisterMbeans);
        return this;
    }

    public HikariDataSourceBuilder isAllowPoolSuspension(String isAllowPoolSuspension) {
        this.properties.put("isAllowPoolSuspension", isAllowPoolSuspension);
        return this;
    }


    public DataSource build() {
        return new HikariDataSource(new HikariConfig(this.properties));
    }
}
