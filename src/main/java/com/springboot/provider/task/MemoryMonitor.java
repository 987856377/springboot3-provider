package com.springboot.provider.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Project test
 * @Package runtime
 * @Author xuzhenkui
 * @Date 2020/5/1 22:54
 */
public class MemoryMonitor {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private ScheduledExecutorService scheduledExecutorService;

    public MemoryMonitor() {
        scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
    }

    //    启动监控服务, 监控内存信息
    public void start() {
        logger.info(String.format("启动监控服务: %s", Thread.currentThread().getName()));
        scheduledExecutorService.scheduleWithFixedDelay(() -> logger.info(String.format("最大内存: %d M, 已分配内存: %d M, 已分配内存中剩余空间: %d M, 最大可用内存: %d M",
                Runtime.getRuntime().maxMemory() / 1024 / 1024,
                Runtime.getRuntime().maxMemory() / 1024 / 1024,
                Runtime.getRuntime().maxMemory() / 1024 / 1024,
                (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory()) / 1024 / 1024
        )), 0, 30, TimeUnit.MINUTES);

    }

    //    释放资源(来源于 flume 源码)
//    主要用于关闭线程池
    public void stop() {
        logger.info(String.format("开始关闭线程: %s", Thread.currentThread().getName()));
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdown();
            try {
                scheduledExecutorService.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (!scheduledExecutorService.isTerminated()) {
                scheduledExecutorService.shutdownNow();
                try {
                    while (!scheduledExecutorService.isTerminated()) {
                        scheduledExecutorService.awaitTermination(10, TimeUnit.SECONDS);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        logger.info(String.format("关闭线程: %s 完成", Thread.currentThread().getName()));
    }
}
