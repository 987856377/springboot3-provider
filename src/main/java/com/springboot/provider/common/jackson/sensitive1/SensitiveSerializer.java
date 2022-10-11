package com.springboot.provider.common.jackson.sensitive1;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * @Description
 * @Project springboot3-provder
 * @Package com.springboot.provider.common.jackson
 * @Author xuzhenkui
 * @Date 2022-08-09 09:59
 */
public class SensitiveSerializer extends StdScalarSerializer<Object> {
    private static final String MASK_CHAR = "*";

    private final SensitiveOperation operation;
    private final String maskChar;

    public SensitiveSerializer(SensitiveOperation operation, String maskChar) {
        super(String.class, false);
        this.operation = operation;
        this.maskChar = maskChar;
    }

    public boolean isEmpty(SerializerProvider prov, Object value) {
        String str = (String) value;
        return str.isEmpty();
    }

    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (Objects.isNull(operation)) {
            String content = SensitiveMode.NONE.operation().mask((String) value, StringUtils.hasText(maskChar) ? maskChar : MASK_CHAR);
            gen.writeString(content);
        } else {
            String content = operation.mask((String) value, StringUtils.hasText(maskChar) ? maskChar : MASK_CHAR);
            gen.writeString(content);
        }
    }

    public final void serializeWithType(Object value, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
        this.serialize(value, gen, provider);
    }

    public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
        return this.createSchemaNode("string", true);
    }

    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
        this.visitStringFormat(visitor, typeHint);
    }
}

