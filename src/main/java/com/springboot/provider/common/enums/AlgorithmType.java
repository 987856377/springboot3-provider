package com.springboot.provider.common.enums;

import com.springboot.provider.common.encrypt.encryptor.*;

/**
 * 算法名称
 *
 * @author 老马
 * @version 4.6.0
 */
public enum AlgorithmType {

    /**
     * 默认走yml配置
     */
    DEFAULT(null),

    /**
     * base64
     */
    BASE64(Base64Encryptor.class),

    /**
     * aes
     */
    AES(AesEncryptor.class),

    /**
     * rsa
     */
    RSA(RsaEncryptor.class),

    /**
     * sm2
     */
    SM2(Sm2Encryptor.class),

    /**
     * sm4
     */
    SM4(Sm4Encryptor.class);

    private final Class<? extends AbstractEncryptor> clazz;

    AlgorithmType(Class<? extends AbstractEncryptor> clazz) {
        this.clazz = clazz;
    }

    public Class<? extends AbstractEncryptor> getClazz() {
        return clazz;
    }
}
