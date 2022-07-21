package com.springboot.provider.module.common.service.impl;

import com.springboot.provider.module.common.AppPayProperties;
import com.springboot.provider.module.common.service.PayService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AppPayProperties.class)
@ConditionalOnProperty(prefix = "application.pay", name = "type", havingValue = "1")
public class WechatPayServiceImpl implements PayService {

    private final AppPayProperties appPayProperties;

    public WechatPayServiceImpl(AppPayProperties appPayProperties) {
        this.appPayProperties = appPayProperties;
    }

    @Override
    public String pay() {
        return "Wechat Payed: appId: " + appPayProperties.getAppId();
    }
}
