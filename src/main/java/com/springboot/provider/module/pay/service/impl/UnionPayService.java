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
 * @create: 2021-01-08 09:28
 **/
@Service
public class UnionPayService extends AbstractPayService implements PayService {

    private final Logger logger = LoggerFactory.getLogger(UnionPayService.class);

//    1. 使用构造函数注册
//    public UnionPayService(){
//        PayStrategyFactory.register(PayStrategy.UNION, this);
//    }

//    2. 使用 @PostConstruct 注册
//    @PostConstruct
//    public void init(){
//        PayStrategyFactory.register(PayStrategy.UNION, this);
//    }

    //    3. 由抽象父类注册
    @Override
    public PayStrategy getStrategy() {
        return PayStrategy.UNION;
    }

    @Override
    public PayService getService() {
        return this;
    }

    @Override
    public Boolean pay() {
        if (valid()) {
            logger.info("PayEnum.UNION = " + PayStrategy.UNION);
            return true;
        }
        return false;
    }
}
