package com.springboot.provider.common.selector;

import com.springboot.provider.common.selector.annotation.EnableBeans;
import com.springboot.provider.common.utils.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @program: springboot-dev
 * @package com.spring.development.common.selector
 * @description
 * @author: XuZhenkui
 * @create: 2021-02-11 11:47
 **/
public class EnableBeansAutoSelector implements ImportSelector {
    private final Logger logger = LoggerFactory.getLogger(EnableBeansAutoSelector.class);

    /**
     * The default enable beans annotation attribute name.
     */
    public static final String ENABLE_BEANS_PACKAGES_ATTRIBUTE_NAME = "packages";

    public static final String ENABLE_BEANS_IS_RECURSION_ATTRIBUTE_NAME = "isRecursion";


    /**
     * The name of the {@link EnableBeans} attribute for the annotation specified by the
     * generic type {@code A}. The default is {@value #ENABLE_BEANS_PACKAGES_ATTRIBUTE_NAME},
     * but subclasses may override in order to customize.
     */
    protected String getEnableBeansPackagesAttributeName() {
        return ENABLE_BEANS_PACKAGES_ATTRIBUTE_NAME;
    }

    protected String getEnableBeansIsRecursionAttributeName() {
        return ENABLE_BEANS_IS_RECURSION_ATTRIBUTE_NAME;
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
        //获取EnableEcho注解的所有属性的value
        Map<String, Object> attributes = importingClassMetadata.getAnnotationAttributes(EnableBeans.class.getName());
        if (attributes == null) {
            return new String[0];
        }
        //获取package属性的value
        String[] packages = (String[]) attributes.get(getEnableBeansPackagesAttributeName());
        boolean isRecursion = (boolean) attributes.get(getEnableBeansIsRecursionAttributeName());

        if (packages == null || packages.length <= 0 || !StringUtils.hasText(packages[0])) {
            return new String[0];
        }
        logger.info("Loading all the classes under package to Spring Context, package name is：" + Arrays.toString(packages));
        Set<String> classNames = new HashSet<>();

        for (String packageName : packages) {
            Set<String> classNameSet = ClassUtils.getClassName(packageName, isRecursion);
            if (classNameSet != null) {
                classNames.addAll(classNameSet);
            }
        }
        //将类打印到日志中
        for (String className : classNames) {
            logger.info(className + " has been loaded to Spring Context");
        }
        String[] returnClassNames = new String[classNames.size()];
        returnClassNames = classNames.toArray(returnClassNames);
        return returnClassNames;
    }
}
