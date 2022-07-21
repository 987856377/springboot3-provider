package com.springboot.provider.common.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Description
 * @Project test
 * @Package proxy.dynamicProxy
 * @Author xuzhenkui
 * @Date 2020/4/19 10:02
 */
public class CGlibProxy implements MethodInterceptor {
    private Object targetClass;

    public Object getProxyInstance(Object targetClass) {
        if (targetClass == null) {
            throw new RuntimeException("target class is null");
        }
        this.targetClass = targetClass;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(targetClass.getClass());
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        long l = System.currentTimeMillis();
        Object result = methodProxy.invokeSuper(o, objects);
        System.out.println("调用方法: " + method.getName() + ", 调用耗时: " + (System.currentTimeMillis() - l) + " ms");
        return result;
    }
}
