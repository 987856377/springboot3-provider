package com.springboot.provider.common.jackson.security;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.crypto.symmetric.SM4;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import java.util.function.Function;

/**
 * @Description
 * @Project bsinterface
 * @Package com.bsoft.bsinterface.utils
 * @Author xuzhenkui
 * @Date 2022-07-04 12:42
 */
@Component
public class SecurityFactory implements InitializingBean {
    private static SM2 sm2;
    private static SM4 sm4;
    private static Table<String, String, Function<String, String>> tables = HashBasedTable.create();

    private final Environment environment;

    public SecurityFactory(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        sm2 = SmUtil.sm2(environment.getProperty("sec.sm2.privateKey"), environment.getProperty("sec.sm2.publicKey"));
        sm4 = SmUtil.sm4(Objects.requireNonNull(environment.getProperty("sec.sm4.key")).getBytes(StandardCharsets.UTF_8));

        tables.put("sm2", "enc", s -> sm2.encryptBase64(s, KeyType.PublicKey));
        tables.put("sm2", "dec", s -> sm2.decryptStr(s, KeyType.PrivateKey));
        tables.put("sm4", "enc", sm4::encryptBase64);
        tables.put("sm4", "dec", sm4::decryptStr);
        tables.put("base64", "enc", s -> Base64.getEncoder().encodeToString(s.getBytes(StandardCharsets.UTF_8)));
        tables.put("base64", "dec", s -> new String(Base64.getDecoder().decode(s.getBytes(StandardCharsets.UTF_8))));
    }

    public static SM2 getSm2() {
        return sm2;
    }

    public static SM4 getSm4() {
        return sm4;
    }

    public static Table<String, String, Function<String, String>> getSecTable() {
        return tables;
    }
}
