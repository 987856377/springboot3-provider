package com.springboot.provider.module.common.service.impl;

import com.springboot.provider.module.common.AppPayProperties;
import com.springboot.provider.module.common.service.PayService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AppPayProperties.class)
@ConditionalOnProperty(prefix = "application.pay", name = "type", havingValue = "0", matchIfMissing = true)
public class UnionPayServiceImpl implements PayService {

    private final AppPayProperties appPayProperties;

    public UnionPayServiceImpl(AppPayProperties appPayProperties) {
        this.appPayProperties = appPayProperties;
    }

    @Override
    public String pay() {
        return "Union Payed: appId: " + appPayProperties.getAppId();
    }
}
