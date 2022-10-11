package com.springboot.provider.common.jackson.sensitive1;

import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;

/**
 * @Description
 * @Project springboot3-provder
 * @Package com.springboot.provider.common.jackson
 * @Author xuzhenkui
 * @Date 2022-08-09 09:58
 */
public class SensitiveAnnotationIntrospector extends NopAnnotationIntrospector {

    @Override
    public Object findSerializer(Annotated am) {
        Sensitive annotation = am.getAnnotation(Sensitive.class);
        if (annotation != null) {
            return new SensitiveSerializer(annotation.maskFunc().operation(), annotation.maskChar());
        }
        return null;
    }

}
