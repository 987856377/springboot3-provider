package com.springboot.provider.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean(name = "masterDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "masterJdbcClient")
    public JdbcClient masterJdbcClient(@Qualifier("masterDataSource") DataSource dataSource) {
        return JdbcClient.create(dataSource);
    }


    @Bean(name = "slaveDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.slave")
    public DataSource slaveDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "slaveJdbcClient")
    public JdbcClient slaveJdbcClient(@Qualifier("slaveDataSource") DataSource dataSource) {
        return JdbcClient.create(dataSource);
    }
}
