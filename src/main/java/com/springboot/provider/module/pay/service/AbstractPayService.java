package com.springboot.provider.module.pay.service;

import com.springboot.provider.module.pay.enums.PayStrategy;
import com.springboot.provider.module.pay.factory.PayStrategyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description
 * @Project springboot-provider
 * @Package com.springboot.provider.module.pay.service
 * @Author xuzhenkui
 * @Date 2021/7/2 15:45
 */
public abstract class AbstractPayService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //    1. 使用构造函数注册
    public AbstractPayService() {
        PayStrategyFactory.register(getStrategy(), getService());
    }

//    2. 使用 @PostConstruct 注册
//    @PostConstruct
//    private void register(){
//        PayStrategyFactory.register(getStrategy(), getService());
//    }

    public abstract PayStrategy getStrategy();

    public abstract PayService getService();


    public Boolean valid() {
        logger.info("AbstractPayService validate pay environment success!");
        return true;
    }
}
