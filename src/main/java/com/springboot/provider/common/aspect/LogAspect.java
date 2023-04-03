package com.springboot.provider.common.aspect;

import cn.hutool.core.lang.id.NanoId;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@Aspect
@Component
public class LogAspect {
    private final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    private final ObjectMapper objectMapper;

    public LogAspect(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Pointcut("execution(public * com.springboot.provider.module.*.controller.*.*(..))")
    public void log() {
    }

    @Around("log()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        String invokeId = NanoId.randomNanoId();

        String parameter;
        if (joinPoint.getArgs().length == 2 && joinPoint.getArgs()[1] instanceof BeanPropertyBindingResult) {
            parameter = objectMapper.writeValueAsString(joinPoint.getArgs()[0]);
        } else {
            parameter = objectMapper.writeValueAsString(joinPoint.getArgs());
        }

        logger.info("\nInvokeId: {} \nRemote Address: {} \nRequest URL: {} \nRequest URI: {} \nParameter: {}",
                invokeId, request.getRemoteAddr(), request.getRequestURL(), request.getRequestURI(), parameter);

        long start = System.currentTimeMillis();
        //调用 proceed() 方法才会真正的执行实际被代理的方法
        Object result = joinPoint.proceed();

        logger.info("\nInvokeId: {} \nReturn: {} \nInvoke Cost: {}",
                invokeId, objectMapper.writeValueAsString(result), (System.currentTimeMillis() - start) + "ms");

        return result;
    }
}
