package com.springboot.provider.common.jackson.security;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Function;

/**
 * 数据脱敏json序列化工具
 *
 * @author Yjoioooo
 */
public class EncryptJsonSerializer extends JsonSerializer<String> implements ContextualSerializer {
    private static final String COLUMN_KEY = "enc";

    private SecurityStrategy strategy;

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        Function<String, String> function = SecurityFactory.getSecTable().get(strategy.toString().toLowerCase(Locale.ROOT), COLUMN_KEY);
        if (function != null) {
            try {
                gen.writeString(function.apply(value));
            } catch (Exception e) {
                gen.writeString(value);
            }
        } else {
            gen.writeString(value);
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        Encrypt annotation = property.getAnnotation(Encrypt.class);
        if (Objects.nonNull(annotation) && Objects.equals(String.class, property.getType().getRawClass())) {
            this.strategy = annotation.strategy();
            return this;
        }
        return prov.findValueSerializer(property.getType(), property);
    }
}
