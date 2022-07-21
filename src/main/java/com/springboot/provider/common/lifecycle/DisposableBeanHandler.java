package com.springboot.provider.common.lifecycle;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

/*
* org.springframework.beans.factory.DisposableBean
* 这个扩展点也只有一个方法：destroy()，其触发时机为当此对象销毁时，会自动执行这个方法。比如说运行applicationContext.registerShutdownHook时，就会触发这个方法。
* */
@Component
public class DisposableBeanHandler implements DisposableBean {
    /**
     * Invoked by the containing {@code BeanFactory} on destruction of a bean.
     *
     * @throws Exception in case of shutdown errors. Exceptions will get logged
     *                   but not rethrown to allow other beans to release their resources as well.
     */
    @Override
    public void destroy() throws Exception {
        System.out.println("[DisposableBean] destroy");
    }
}
