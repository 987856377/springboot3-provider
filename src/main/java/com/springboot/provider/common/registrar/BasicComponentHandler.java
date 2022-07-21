package com.springboot.provider.common.registrar;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * @Description
 * @Project springboot-provider
 * @Package com.springboot.provider.common.registrar
 * @Author xuzhenkui
 * @Date 2021/10/18 13:20
 */
public abstract class BasicComponentHandler {
    private final Class<? extends Annotation> annotationType;

    private final TypeFilter typeFilter;

    protected BasicComponentHandler(Class<? extends Annotation> annotationType) {
        this.typeFilter = new AnnotationTypeFilter(annotationType);
        this.annotationType = annotationType;
    }

    TypeFilter getTypeFilter() {
        return this.typeFilter;
    }

    void handle(AnnotatedBeanDefinition beanDefinition, BeanDefinitionRegistry registry) {
        Map<String, Object> attributes = beanDefinition.getMetadata()
                .getAnnotationAttributes(this.annotationType.getName());
        if (attributes != null) {
            doHandle(attributes, beanDefinition, registry);
        }
    }

    protected abstract void doHandle(Map<String, Object> attributes, AnnotatedBeanDefinition beanDefinition, BeanDefinitionRegistry registry);
}
