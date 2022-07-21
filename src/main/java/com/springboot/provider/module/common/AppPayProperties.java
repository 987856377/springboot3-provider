package com.springboot.provider.module.common;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application.pay")
public class AppPayProperties {
    private String type;
    private String appId;
    private String appSecret;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    @Override
    public String toString() {
        return "AppPayProperties{" +
                "type='" + type + '\'' +
                ", appId='" + appId + '\'' +
                ", appSecret='" + appSecret + '\'' +
                '}';
    }
}
