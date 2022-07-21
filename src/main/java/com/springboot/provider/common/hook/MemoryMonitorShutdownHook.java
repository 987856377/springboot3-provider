package com.springboot.provider.common.hook;

import com.springboot.provider.common.holder.ThreadPoolExecutorHolder;
import com.springboot.provider.task.MemoryMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Project test
 * @Package runtime
 * @Author xuzhenkui
 * @Date 2020/5/2 0:14
 */
@Component
public class MemoryMonitorShutdownHook {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    protected void init(){
        MemoryMonitor memoryMonitor = new MemoryMonitor();
        memoryMonitor.start();

//        添加钩子, 实现优雅停服
        final MemoryMonitor appReference = memoryMonitor;
        Runtime.getRuntime().addShutdownHook(new Thread("memory-monitor-shutdown-hook"){
            @Override
            public void run() {
                logger.info(Thread.currentThread().getName() + " 线程接收到退出信号, 开始释放资源...");
                appReference.stop();

//                关闭线程池
                ExecutorService threadPoolExecutor = ThreadPoolExecutorHolder.getThreadPoolExecutor();
                threadPoolExecutor.shutdown();
                logger.info("threadPoolExecutor.isShutdown() = " + threadPoolExecutor.isShutdown());
                try {
                    threadPoolExecutor.awaitTermination(3, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                logger.info("threadPoolExecutor.isTerminated() = " + threadPoolExecutor.isTerminated());
                if(!threadPoolExecutor.isTerminated()){
                    threadPoolExecutor.shutdownNow();
                    try {
                        while (!threadPoolExecutor.isTerminated()){
                            threadPoolExecutor.awaitTermination(10,TimeUnit.SECONDS);
                            logger.info("threadPoolExecutor.isTerminated() = " + threadPoolExecutor.isTerminated());
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
