package com.springboot.provider.common.lifecycle;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/*
* org.springframework.boot.ApplicationRunnerHandler
* 这个接口也只有一个方法：run(ApplicationArguments args)，触发时机为整个项目启动完毕后，自动执行。如果有多个ApplicationRunnerHandler，可以利用@Order来进行排序。
* CommandLineRunner和ApplicationRunner两个接口参数不同，其他大体相同，可根据实际需求选择合适的接口使用。
* CommandLineRunner接口中run方法的参数为String数组，ApplicationRunner中run方法的参数为ApplicationArguments。
*
* 使用场景：用户扩展此接口，进行启动项目之后一些业务的预处理。
* */
@Component
public class ApplicationRunnerHandler implements ApplicationRunner {
    /**
     * Callback used to run the bean.
     *
     * @param args incoming application arguments
     * @throws Exception on error
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("[ApplicationRunner] run");
    }
}
