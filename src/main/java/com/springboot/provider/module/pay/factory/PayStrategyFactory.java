package com.springboot.provider.module.pay.factory;

import com.springboot.provider.module.pay.enums.PayStrategy;
import com.springboot.provider.module.pay.service.PayService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: springboot-provider
 * @package com.springboot.provider.module.pay.factory
 * @description
 * @author: XuZhenkui
 * @create: 2021-01-08 09:22
 **/
public class PayStrategyFactory {
    private static final Map<PayStrategy, PayService> PAY_MAP = new ConcurrentHashMap<>();

    public static void register(PayStrategy type, PayService payService) {
        if (type != null) {
            if (PAY_MAP.containsKey(type)) {
                throw new RuntimeException("PayStrategy [" + type + "] already registered!!!");
            } else {
                PAY_MAP.put(type, payService);
            }
        }
    }

    public static PayService get(PayStrategy type) {
        if (!PAY_MAP.containsKey(type)) {
            throw new RuntimeException("PayStrategy [" + type + "] not registered!!!");
        }
        return PAY_MAP.get(type);
    }
}
