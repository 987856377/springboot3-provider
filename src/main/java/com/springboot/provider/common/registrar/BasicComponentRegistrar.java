package com.springboot.provider.common.registrar;

import com.springboot.provider.common.registrar.annotation.BasicComponentScan;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @Description
 * @Project springboot-provider
 * @Package com.springboot.provider.common.handler
 * @Author xuzhenkui
 * @Date 2021/10/18 10:26
 */
public class BasicComponentRegistrar implements ImportBeanDefinitionRegistrar {

    private static final String BEAN_NAME = "basicComponentRegistrarPostProcessor";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Set<String> packagesToScan = getPackagesToScan(importingClassMetadata);

        addPostProcessor(registry, packagesToScan);
    }

    private void addPostProcessor(BeanDefinitionRegistry registry, Set<String> packagesToScan) {
        BasicComponentRegistrar.ComponentRegistrarPostProcessorBeanDefinition definition = new BasicComponentRegistrar.ComponentRegistrarPostProcessorBeanDefinition(packagesToScan);
        registry.registerBeanDefinition(BEAN_NAME, definition);
    }

    private Set<String> getPackagesToScan(AnnotationMetadata metadata) {
        AnnotationAttributes attributes = AnnotationAttributes
                .fromMap(metadata.getAnnotationAttributes(BasicComponentScan.class.getName()));
        assert attributes != null;
        String[] basePackages = attributes.getStringArray("basePackages");
        Class<?>[] basePackageClasses = attributes.getClassArray("basePackageClasses");
        Set<String> packagesToScan = new LinkedHashSet<>(Arrays.asList(basePackages));
        for (Class<?> basePackageClass : basePackageClasses) {
            packagesToScan.add(ClassUtils.getPackageName(basePackageClass));
        }
        if (packagesToScan.isEmpty()) {
            packagesToScan.add(ClassUtils.getPackageName(metadata.getClassName()));
        }
        return packagesToScan;
    }

    static final class ComponentRegistrarPostProcessorBeanDefinition extends GenericBeanDefinition {

        private Set<String> packageNames = new LinkedHashSet<>();

        ComponentRegistrarPostProcessorBeanDefinition(Collection<String> packageNames) {
            setBeanClass(BasicComponentRegistrarPostProcessor.class);
            setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
            addPackageNames(packageNames);
        }

        @Override
        public Supplier<?> getInstanceSupplier() {
            return () -> new BasicComponentRegistrarPostProcessor(this.packageNames);
        }

        private void addPackageNames(Collection<String> additionalPackageNames) {
            this.packageNames.addAll(additionalPackageNames);
        }

    }
}
