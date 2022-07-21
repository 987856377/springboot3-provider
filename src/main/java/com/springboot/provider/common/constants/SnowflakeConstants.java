package com.springboot.provider.common.constants;

import cn.hutool.core.lang.generator.SnowflakeGenerator;

/**
 * @Description
 * @Project springboot-provider
 * @Package com.springboot.provider.common.constants
 * @Author xuzhenkui
 * @Date 2022-07-19 21:55
 */
public class SnowflakeConstants {
    private static final SnowflakeGenerator SNOWFLAKE_GENERATOR = new SnowflakeGenerator();

    public static Long next() {
        return SNOWFLAKE_GENERATOR.next();
    }
}
