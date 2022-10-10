package com.springboot.provider.common.aspect;

import cn.hutool.core.lang.id.NanoId;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@Aspect
@Component
public class LogAspect {
    private final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    private final Gson gson = new Gson();

    @Pointcut("execution(public * com.springboot.provider.module.*.controller.*.*(..))")
    public void log() {
    }

    @Around("log()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        String invokeId = NanoId.randomNanoId();

        logger.info("\nInvokeId: {} \nRemote Address: {} \nRequest URL: {} \nRequest URI: {} \nParameter: {}",
                invokeId, request.getRemoteAddr(), request.getRequestURL(), request.getRequestURI(), gson.toJson(joinPoint.getArgs()));

        long start = System.currentTimeMillis();
        //调用 proceed() 方法才会真正的执行实际被代理的方法
        Object result = joinPoint.proceed();

        logger.info("\nInvokeId: {} \nReturn: {} \nInvoke Cost: {}",
                invokeId, gson.toJson(result), (System.currentTimeMillis() - start) + "ms");

        return result;
    }
}
