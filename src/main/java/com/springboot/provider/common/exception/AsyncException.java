package com.springboot.provider.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

public class AsyncException implements AsyncUncaughtExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Handle the given uncaught exception thrown from an asynchronous method.
     *
     * @param ex     the exception thrown from the asynchronous method
     * @param method the asynchronous method
     * @param params the parameters used to invoked the method
     */
    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        logger.error("异步执行错误, message={}", ex.getMessage());
        logger.error("发生错误的方法: {}, 参数: {}", method.getName(), params);
    }
}
