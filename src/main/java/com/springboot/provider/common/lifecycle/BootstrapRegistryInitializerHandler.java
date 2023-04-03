package com.springboot.provider.common.lifecycle;

import javassist.*;
import org.springframework.boot.BootstrapRegistry;
import org.springframework.boot.BootstrapRegistryInitializer;

/**
 * @Description
 * @Project springboot-provider
 * @Package com.springboot.provider.common.handler
 * @Author xuzhenkui
 * @Date 2021/6/2 16:27
 */
public class BootstrapRegistryInitializerHandler implements BootstrapRegistryInitializer {
    /**
     * Initialize the given {@link BootstrapRegistry} with any required registrations.
     *
     * @param registry the registry to initialize
     */
    @Override
    public void initialize(BootstrapRegistry registry) {
        System.out.println("[BootstrapRegistryInitializer] initialize");

        ClassPool pool = ClassPool.getDefault();
        try {
            String classname = "com.springboot.provider.module.common.AppPayProperties";
            pool.insertClassPath(new ClassClassPath(this.getClass()));
            CtClass ctClass = pool.get(classname);

            // 在display后追加一行
            CtMethod sayHello = ctClass.getDeclaredMethod("display");
            sayHello.insertAfter("System.out.println(\"I'm very happy now!\");");

            // 添加calc方法
            CtMethod calcMethod = CtNewMethod.make("public int calc(int a, int b) { return a + b;}", ctClass);
            ctClass.addMethod(calcMethod);

            // 重新生成class
            ctClass.toClass();
            ctClass.freeze();
//            ctClass.writeFile();
            ctClass.detach();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
