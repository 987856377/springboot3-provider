package com.springboot.provider.module.pay.enums;

import java.util.Arrays;

/**
 * @program: springboot-provider
 * @package com.springboot.provider.module.common
 * @description
 * @author: XuZhenkui
 * @create: 2021-01-08 09:12
 **/
public enum PayStrategy {
    UNION("1", "unionPay", "银联支付"),
    WECHAT("2", "wechatPay", "微信支付"),
    ALI("3", "aliPay", "支付宝支付");

    private String key;
    private String value;
    private String remark;

    PayStrategy(String key, String value, String remark) {
        this.key = key;
        this.value = value;
        this.remark = remark;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    //根据key获取value
    public static String getValueByKey(String key) {
        return getEnumByKey(key).getValue();
    }

    //根据key获取枚举
    public static PayStrategy getEnumByKey(String key) {
        return Arrays.stream(PayStrategy.values()).filter(item -> item.getKey().equals(key)).findFirst().orElse(UNION);
    }


    @Override
    public String toString() {
        return "PayStrategy{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", remark='" + remark + '\'' +
                "} " + super.toString();
    }
}
