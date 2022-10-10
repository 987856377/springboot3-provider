package com.springboot.provider.common.holder;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: springboot-provider
 * @package com.springboot.provider.common.holder
 * @description
 * @author: XuZhenkui
 * @create: 2021-02-23 17:18
 **/
public class JdbcTemplateHolder {
    private static final ConcurrentHashMap<String, JdbcTemplate> JDBC_TEMPLATE_MAP = new ConcurrentHashMap<>();

    /**
     * 根据数据源名称获取数据源
     *
     * @param dsName 数据库名称
     * @return
     */
    public static JdbcTemplate getJdbcTemplate(String dsName) {
        if (StringUtils.hasText(dsName)) {
            return JDBC_TEMPLATE_MAP.get(dsName);
        }
        return null;
    }

    /**
     * 添加数据源
     *
     * @param dsName     数据库名称
     * @param dataSource 数据源
     */
    public static Boolean addJdbcTemplate(String dsName, DataSource dataSource) {
        if (StringUtils.hasText(dsName) && dataSource != null) {
//        如果传入key对应的value已经存在，就返回存在的value，不进行替换。如果不存在，就添加key和value，返回null
            return JDBC_TEMPLATE_MAP.putIfAbsent(dsName, new JdbcTemplate(dataSource)) == null;
        }
        return false;
    }

    /**
     * 修改数据源
     *
     * @param dsName     数据库名称
     * @param dataSource 数据源
     */
    public static Boolean updateJdbcTemplate(String dsName, DataSource dataSource) {
        if (StringUtils.hasText(dsName) && dataSource != null) {
            JDBC_TEMPLATE_MAP.put(dsName, new JdbcTemplate(dataSource));
            return true;
        }
        return false;
    }

    /**
     * 删除数据源
     *
     * @param dsName     数据库名称
     * @param dataSource 数据源
     */
    public static Boolean removeJdbcTemplate(String dsName, DataSource dataSource) {
        if (StringUtils.hasText(dsName) && dataSource != null) {
            JDBC_TEMPLATE_MAP.remove(dsName);
            return true;
        }
        return false;
    }
}
