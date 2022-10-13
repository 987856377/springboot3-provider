package com.springboot.provider.common.jackson.security;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Function;

/**
 * 数据脱敏json序列化工具
 *
 * @author Yjoioooo
 */
public class DecryptJsonDeserializer extends JsonDeserializer<String> implements ContextualDeserializer {
    private static final String COLUMN_KEY = "dec";

    private SecurityStrategy strategy;

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        Function<String, String> function = SecurityFactory.getSecTable().get(strategy.toString().toLowerCase(Locale.ROOT), COLUMN_KEY);
        try {
            if (function != null) {
                return function.apply(p.getValueAsString());
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        Decrypt annotation = property.getAnnotation(Decrypt.class);
        if (Objects.nonNull(annotation) && Objects.equals(String.class, property.getType().getRawClass())) {
            this.strategy = annotation.strategy();
            return this;
        }
        return ctxt.findContextualValueDeserializer(property.getType(), property);
    }
}
