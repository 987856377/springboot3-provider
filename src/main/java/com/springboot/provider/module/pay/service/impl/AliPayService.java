package com.springboot.provider.module.pay.service.impl;

import com.springboot.provider.module.pay.enums.PayStrategy;
import com.springboot.provider.module.pay.service.AbstractPayService;
import com.springboot.provider.module.pay.service.PayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @program: springboot-provider
 * @package com.springboot.provider.module.common.service.impl
 * @description
 * @author: XuZhenkui
 * @create: 2021-01-08 09:27
 **/
@Service
public class AliPayService extends AbstractPayService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

//    1. 使用构造函数注册
//    public AliPayService(){
//        PayStrategyFactory.register(PayStrategy.ALI, this);
//    }

//    2. 使用 @PostConstruct 注册
//    @PostConstruct
//    public void init(){
//        PayStrategyFactory.register(PayStrategy.ALI, this);
//    }


    //    3. 由抽象父类注册
    @Override
    public PayStrategy getStrategy() {
        return PayStrategy.ALI;
    }

    @Override
    public Boolean pay() {
        if (valid()) {
            logger.info("PayEnum.ALI = " + PayStrategy.ALI);
            return true;
        }
        return false;
    }

    @Override
    public Boolean valid() {
        if (super.valid()) {
            logger.info("AliPayService validate pay environment success!");
            return true;
        }
        return false;
    }
}
