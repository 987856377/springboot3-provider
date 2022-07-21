package com.springboot.provider.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * xss过滤 配置属性
 *
 */
@Component
@ConfigurationProperties(prefix = "xss")
public class XssProperties {

    /**
     * 过滤开关
     */
    private String enabled;

    /**
     * 排除链接（多个用逗号分隔）
     */
    private String excludes;

    /**
     * 匹配链接
     */
    private String urlPatterns;

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getExcludes() {
        return excludes;
    }

    public void setExcludes(String excludes) {
        this.excludes = excludes;
    }

    public String getUrlPatterns() {
        return urlPatterns;
    }

    public void setUrlPatterns(String urlPatterns) {
        this.urlPatterns = urlPatterns;
    }
}
