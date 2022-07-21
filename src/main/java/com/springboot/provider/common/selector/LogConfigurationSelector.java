package com.springboot.provider.common.selector;

import com.springboot.provider.common.selector.annotation.EnableLog;
import com.springboot.provider.common.enums.LogMode;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * @program: springboot-dev
 * @package com.spring.development.common.selector
 * @description
 * @author: XuZhenkui
 * @create: 2020-11-19 10:24
 **/
public class LogConfigurationSelector implements ImportSelector {
    public static final String LOG_INFO_CONFIG = "com.springboot.provider.common.selector.config.LogInfoConfig";
    public static final String LOG_ERROR_CONFIG = "com.springboot.provider.common.selector.config.LogErrorConfig";

    /**
     * The default advice mode attribute name.
     */
    public static final String DEFAULT_ADVICE_MODE_ATTRIBUTE_NAME = "mode";


    /**
     * The name of the {@link AdviceMode} attribute for the annotation specified by the
     * generic type {@code A}. The default is {@value #DEFAULT_ADVICE_MODE_ATTRIBUTE_NAME},
     * but subclasses may override in order to customize.
     */
    protected String getAdviceModeAttributeName() {
        return DEFAULT_ADVICE_MODE_ATTRIBUTE_NAME;
    }

    /**
     * Select and return the names of which class(es) should be imported based on
     * the {@link AnnotationMetadata} of the importing @{@link Configuration} class.
     *
     * @param importingClassMetadata
     * @return the class names, or an empty array if none
     */
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(EnableLog.class.getName());
        LogMode mode = (LogMode) annotationAttributes.get(getAdviceModeAttributeName());
        switch (mode) {
            case INFO:
                return new String[]{LOG_INFO_CONFIG};
            case ERROR:
                return new String[]{LOG_ERROR_CONFIG};
        }
        return new String[]{LOG_INFO_CONFIG};
    }
}
