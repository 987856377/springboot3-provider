package com.springboot.provider.common.encrypt;

import cn.hutool.core.util.ReflectUtil;
import com.springboot.provider.common.annotation.EncryptField;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 加密管理类
 *
 * @author 老马
 * @version 4.6.0
 */
public class EncryptorManager {
    /**
     * 缓存加密器
     */
    private static final Map<EncryptContext, IEncryptor> ENCRYPTOR_MAP = new ConcurrentHashMap<>();

    /**
     * 类加密字段缓存
     */
    private static final Map<Class<?>, Set<Field>> FIELD_CACHE = new ConcurrentHashMap<>();

    /**
     * 获取类加密字段缓存
     */
    public Set<Field> getFieldCache(Class<?> sourceClazz) {
        return FIELD_CACHE.computeIfAbsent(sourceClazz, clazz -> {
            Field[] declaredFields = clazz.getDeclaredFields();
            Set<Field> fieldSet = Arrays.stream(declaredFields).filter(field ->
                            field.isAnnotationPresent(EncryptField.class) && field.getType() == String.class)
                    .collect(Collectors.toSet());
            for (Field field : fieldSet) {
                field.setAccessible(true);
            }
            return fieldSet;
        });
    }

    /**
     * 注册加密执行者到缓存
     *
     * @param encryptContext 加密执行者需要的相关配置参数
     */
    public IEncryptor registAndGetEncryptor(EncryptContext encryptContext) {
        if (ENCRYPTOR_MAP.containsKey(encryptContext)) {
            return ENCRYPTOR_MAP.get(encryptContext);
        }
        IEncryptor encryptor = ReflectUtil.newInstance(encryptContext.getAlgorithm().getClazz(), encryptContext);
        ENCRYPTOR_MAP.put(encryptContext, encryptor);
        return encryptor;
    }

    /**
     * 移除缓存中的加密执行者
     *
     * @param encryptContext 加密执行者需要的相关配置参数
     */
    public void removeEncryptor(EncryptContext encryptContext) {
        ENCRYPTOR_MAP.remove(encryptContext);
    }

    /**
     * 根据配置进行加密。会进行本地缓存对应的算法和对应的秘钥信息。
     *
     * @param value          待加密的值
     * @param encryptContext 加密相关的配置信息
     */
    public String encrypt(String value, EncryptContext encryptContext) {
        IEncryptor encryptor = this.registAndGetEncryptor(encryptContext);
        return encryptor.encrypt(value, encryptContext.getEncode());
    }

    /**
     * 根据配置进行解密
     *
     * @param value          待解密的值
     * @param encryptContext 加密相关的配置信息
     */
    public String decrypt(String value, EncryptContext encryptContext) {
        IEncryptor encryptor = this.registAndGetEncryptor(encryptContext);
        return encryptor.decrypt(value);
    }

}
