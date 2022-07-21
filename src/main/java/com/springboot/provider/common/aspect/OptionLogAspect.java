package com.springboot.provider.common.aspect;

import com.springboot.provider.common.annotation.OptionLog;
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

/**
 * @Description
 * @Project springboot-provider
 * @Package com.springboot.provider.common.aspect
 * @Author xuzhenkui
 * @Date 2021/9/6 14:18
 */
@Aspect
@Component
public class OptionLogAspect {
    private final Logger logger = LoggerFactory.getLogger(OptionLogAspect.class);

    @Pointcut(value = "@annotation(com.springboot.provider.common.annotation.OptionLog)")
    public void methodPointcut() {
    }

    /**
     * 对带注解@OpLog的方法进行切面，并获取到注解的属性值
     */
    @Around(value = "methodPointcut() && @annotation(optionLog)", argNames = "point,optionLog")
    public Object around(ProceedingJoinPoint point, OptionLog optionLog) throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        long l = System.currentTimeMillis();
        //调用 proceed() 方法才会真正的执行实际被代理的方法
        Object result = point.proceed();

        logger.info("\nRemote Address: {} \nRequest URL: {} \nRequest URI: {} \nOptionLog.mode: {} \nOptionLog.source: {} \nInvoke Cost: {}",
                request.getRemoteAddr(), request.getRequestURL(), request.getRequestURI(), optionLog.mode(), optionLog.source(), (System.currentTimeMillis() - l) + "ms");

        return result;
    }

}
