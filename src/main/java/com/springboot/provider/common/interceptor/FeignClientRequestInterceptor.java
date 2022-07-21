package com.springboot.provider.common.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class FeignClientRequestInterceptor implements RequestInterceptor {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public void apply(RequestTemplate requestTemplate) {
        LOGGER.info("FeignClientRequestInterceptor: name: {}, url: {}, method: {}", requestTemplate.feignTarget().name(), requestTemplate.feignTarget().url(), requestTemplate.url());
        requestTemplate.header(HttpHeaders.AUTHORIZATION, "Bearer Token");
    }
}
