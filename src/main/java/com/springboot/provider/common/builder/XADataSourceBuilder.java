package com.springboot.provider.common.builder;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.boot.context.properties.source.ConfigurationPropertyNameAliases;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.jdbc.XADataSourceWrapper;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import javax.sql.XADataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: springboot-provider
 * @package com.springboot.provider.common.holder
 * @description
 * @author: XuZhenkui
 * @create: 2020-12-10 10:24
 **/
@Component
public class XADataSourceBuilder {

    public DataSource dataSource(XADataSourceWrapper wrapper, DataSourceProperties properties,
                                 ObjectProvider<XADataSource> xaDataSource) throws Exception {
        return wrapper.wrapDataSource(xaDataSource.getIfAvailable(() -> createXaDataSource(properties)));
    }

    private XADataSource createXaDataSource(DataSourceProperties properties) {
        String className = properties.getXa().getDataSourceClassName();
        if (!StringUtils.hasLength(className)) {
            className = DatabaseDriver.fromJdbcUrl(properties.determineUrl()).getXaDataSourceClassName();
        }
        Assert.state(StringUtils.hasLength(className), "No XA DataSource class name specified");
        XADataSource dataSource = createXaDataSourceInstance(className);
        bindXaProperties(dataSource, properties);
        return dataSource;
    }

    private XADataSource createXaDataSourceInstance(String className) {
        try {
            Class<?> dataSourceClass = ClassUtils.forName(className, this.getClass().getClassLoader());
            Object instance = BeanUtils.instantiateClass(dataSourceClass);
            Assert.isInstanceOf(XADataSource.class, instance);
            return (XADataSource) instance;
        }
        catch (Exception ex) {
            throw new IllegalStateException("Unable to create XADataSource instance from '" + className + "'");
        }
    }

    private void bindXaProperties(XADataSource target, DataSourceProperties dataSourceProperties) {
        Binder binder = new Binder(getBinderSource(dataSourceProperties));
        binder.bind(ConfigurationPropertyName.EMPTY, Bindable.ofInstance(target));
    }

    private ConfigurationPropertySource getBinderSource(DataSourceProperties dataSourceProperties) {
        Map<Object, Object> properties = new HashMap<Object, Object>();
        properties.putAll(dataSourceProperties.getXa().getProperties());
        properties.computeIfAbsent("user", (key) -> dataSourceProperties.determineUsername());
        properties.computeIfAbsent("password", (key) -> dataSourceProperties.determinePassword());
        try {
            properties.computeIfAbsent("url", (key) -> dataSourceProperties.determineUrl());
        }
        catch (DataSourceBeanCreationException ex) {
            // Continue as not all XA DataSource's require a URL
        }
        MapConfigurationPropertySource source = new MapConfigurationPropertySource(properties);
        ConfigurationPropertyNameAliases aliases = new ConfigurationPropertyNameAliases();
        aliases.addAliases("user", "username");
        return source.withAliases(aliases);
    }

    static class DataSourceBeanCreationException extends BeanCreationException {

        private final DataSourceProperties properties;

        private final EmbeddedDatabaseConnection connection;

        DataSourceBeanCreationException(String message, DataSourceProperties properties,
                                        EmbeddedDatabaseConnection connection) {
            super(message);
            this.properties = properties;
            this.connection = connection;
        }

        DataSourceProperties getProperties() {
            return this.properties;
        }

        EmbeddedDatabaseConnection getConnection() {
            return this.connection;
        }

    }
}
