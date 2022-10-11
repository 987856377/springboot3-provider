package com.springboot.provider.common.jackson.sensitive1;

public interface SensitiveOperation {
    String mask(String content, String maskChar);
}
