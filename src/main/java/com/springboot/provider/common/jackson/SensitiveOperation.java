package com.springboot.provider.common.jackson;

public interface SensitiveOperation {
    String mask(String content, String maskChar);
}
