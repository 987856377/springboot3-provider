package com.springboot.provider.common.registrar;

import com.springboot.provider.common.registrar.handler.BasicFilterHandler;
import com.springboot.provider.common.registrar.handler.BasicListenerHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @Description
 * @Project springboot-provider
 * @Package com.springboot.provider.common.registrar
 * @Author xuzhenkui
 * @Date 2021/10/18 12:03
 */
public class BasicComponentRegistrarPostProcessor implements BeanFactoryPostProcessor, ApplicationContextAware {

    private static final List<BasicComponentHandler> HANDLERS;

    static {
        List<BasicComponentHandler> servletComponentHandlers = new ArrayList<>();
        servletComponentHandlers.add(new BasicFilterHandler());
        servletComponentHandlers.add(new BasicListenerHandler());
        HANDLERS = Collections.unmodifiableList(servletComponentHandlers);
    }


    private final Set<String> packagesToScan;

    private ApplicationContext applicationContext;

    public BasicComponentRegistrarPostProcessor(Set<String> packagesToScan) {
        this.packagesToScan = packagesToScan;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        if (isRunningInEmbeddedWebServer()) {
            ClassPathScanningCandidateComponentProvider componentProvider = createComponentProvider();
            for (String packageToScan : this.packagesToScan) {
                scanPackage(componentProvider, packageToScan);
            }
        }
    }

    private boolean isRunningInEmbeddedWebServer() {
        return this.applicationContext instanceof WebApplicationContext
                && ((WebApplicationContext) this.applicationContext).getServletContext() == null;
    }

    private void scanPackage(ClassPathScanningCandidateComponentProvider componentProvider, String packageToScan) {
        for (BeanDefinition candidate : componentProvider.findCandidateComponents(packageToScan)) {
            if (candidate instanceof AnnotatedBeanDefinition) {
                for (BasicComponentHandler handler : HANDLERS) {
                    handler.handle(((AnnotatedBeanDefinition) candidate), (BeanDefinitionRegistry) this.applicationContext);
                }
            }
        }
    }

    private ClassPathScanningCandidateComponentProvider createComponentProvider() {
        ClassPathScanningCandidateComponentProvider componentProvider = new ClassPathScanningCandidateComponentProvider(false);
        componentProvider.setEnvironment(this.applicationContext.getEnvironment());
        componentProvider.setResourceLoader(this.applicationContext);
        for (BasicComponentHandler handler : HANDLERS) {
            componentProvider.addIncludeFilter(handler.getTypeFilter());
        }
        return componentProvider;
    }

    public Set<String> getPackagesToScan() {
        return packagesToScan;
    }
}
