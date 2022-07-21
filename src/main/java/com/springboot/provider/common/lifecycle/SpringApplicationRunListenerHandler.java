package com.springboot.provider.common.lifecycle;

import org.springframework.boot.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.time.Duration;

/**
 * @Description
 * @Project springboot-provider
 * @Package com.springboot.provider.common.handler
 * @Author xuzhenkui
 * @Date 2021-05-27 10:18
 */
public class SpringApplicationRunListenerHandler implements SpringApplicationRunListener {

    private final SpringApplication application;
    private final String[] args;

    public SpringApplicationRunListenerHandler(SpringApplication application, String[] args) {
        this.application = application;
        this.args = args;
    }


    /**
     * Called immediately when the run method has first started. Can be used for very
     * early initialization.
     *
     * @param bootstrapContext the bootstrap context
     */
    @Override
    public void starting(ConfigurableBootstrapContext bootstrapContext) {
        System.out.println("[SpringApplicationRunListener] starting");
    }

    /**
     * Called once the environment has been prepared, but before the
     * {@link ApplicationContext} has been created.
     *
     * @param bootstrapContext the bootstrap context
     * @param environment      the environment
     */
    @Override
    public void environmentPrepared(ConfigurableBootstrapContext bootstrapContext, ConfigurableEnvironment environment) {
        System.out.println("[SpringApplicationRunListener] environmentPrepared");
    }

    /**
     * Called once the {@link ApplicationContext} has been created and prepared, but
     * before sources have been loaded.
     *
     * @param context the application context
     */
    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        System.out.println("[SpringApplicationRunListener] contextPrepared");
    }

    /**
     * Called once the application context has been loaded but before it has been
     * refreshed.
     *
     * @param context the application context
     */
    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        System.out.println("[SpringApplicationRunListener] contextLoaded");
    }


    /**
     * The context has been refreshed and the application has started but
     * {@link CommandLineRunner CommandLineRunners} and {@link ApplicationRunner
     * ApplicationRunners} have not been called.
     *
     * @param context   the application context.
     * @param timeTaken the time taken to start the application or {@code null} if unknown
     * @since 2.6.0
     */
    @Override
    public void started(ConfigurableApplicationContext context, Duration timeTaken) {
        System.out.println("[SpringApplicationRunListener] started");
    }

    /**
     * Called immediately before the run method finishes, when the application context has
     * been refreshed and all {@link CommandLineRunner CommandLineRunners} and
     * {@link ApplicationRunner ApplicationRunners} have been called.
     *
     * @param context   the application context.
     * @param timeTaken the time taken for the application to be ready or {@code null} if
     *                  unknown
     * @since 2.6.0
     */
    @Override
    public void ready(ConfigurableApplicationContext context, Duration timeTaken) {
        System.out.println("[SpringApplicationRunListener] ready");
    }

    /**
     * Called when a failure occurs when running the application.
     *
     * @param context   the application context or {@code null} if a failure occurred before
     *                  the context was created
     * @param exception the failure
     * @since 2.0.0
     */
    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        System.out.println("[SpringApplicationRunListener] failed");
    }
}
