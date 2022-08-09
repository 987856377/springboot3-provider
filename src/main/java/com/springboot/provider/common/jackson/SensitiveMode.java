package com.springboot.provider.common.jackson;

import org.springframework.util.StringUtils;

public enum SensitiveMode {

    NONE((content, maskChar) -> content),

    MID((content, maskChar) -> {
        if (StringUtils.hasText(content)) {
            StringBuilder sb = new StringBuilder();
            sb.append(content.charAt(0));
            if (content.length() == 2) {
                sb.append(maskChar);
            } else if (content.length() >= 3) {
                for (int i = 1; i < content.length() - 1; i++) {
                    sb.append(maskChar);
                }
                sb.append(content.charAt(content.length() - 1));
            }
            return sb.toString();
        } else {
            return content;
        }
    }),

    ALL((content, maskChar) -> {
        if (StringUtils.hasText(content)) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < content.length(); i++) {
                sb.append(maskChar);
            }
            return sb.toString();
        } else {
            return content;
        }
    });

    private final SensitiveOperation operation;

    SensitiveMode(SensitiveOperation operation) {
        this.operation = operation;
    }

    public SensitiveOperation operation() {
        return operation;
    }
}
