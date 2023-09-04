package com.springboot.provider.common.encrypt;

import com.springboot.provider.common.enums.AlgorithmType;
import com.springboot.provider.common.enums.EncodeType;

import java.util.Objects;

/**
 * 加密上下文 用于encryptor传递必要的参数。
 *
 * @author 老马
 * @version 4.6.0
 */
public class EncryptContext {

    /**
     * 默认算法
     */
    private AlgorithmType algorithm;

    /**
     * 安全秘钥
     */
    private String password;

    /**
     * 公钥
     */
    private String publicKey;

    /**
     * 私钥
     */
    private String privateKey;

    /**
     * 编码方式，base64/hex
     */
    private EncodeType encode;

    public AlgorithmType getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(AlgorithmType algorithm) {
        this.algorithm = algorithm;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public EncodeType getEncode() {
        return encode;
    }

    public void setEncode(EncodeType encode) {
        this.encode = encode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EncryptContext that = (EncryptContext) o;
        return algorithm == that.algorithm && Objects.equals(password, that.password) && Objects.equals(publicKey, that.publicKey) && Objects.equals(privateKey, that.privateKey) && encode == that.encode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(algorithm, password, publicKey, privateKey, encode);
    }
}
