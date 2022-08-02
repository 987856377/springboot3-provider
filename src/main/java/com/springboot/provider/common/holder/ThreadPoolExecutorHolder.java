package com.springboot.provider.common.holder;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * @Description
 * @Project development
 * @Package com.spring.development.util
 * @Author xuzhenkui
 * @Date 2020/5/10 8:24
 */
public class ThreadPoolExecutorHolder {

    //    核心线程数量: 取 CPU 核心数 + 1 个
    private final static int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors() + 1;

    //    最大线程池数量:
//    1. CPU 密集型任务配置尽可能少的线程数量, 一般公式: CPU 核心数 + 1个线程的线程池
//    2. IO 密集型: 1. 配置尽可能多的线程: CPU核心数 * 2;  2. CPU核心数 / 1 - 阻塞系数(0.8 - 0.9) 例: 8 / (1-0.9)
    private final static int MAXIMUM_POOL_SIZE = CORE_POOL_SIZE * 2;

    private final static long KEEP_ALIVE_TIME = 30L;

    private final static TimeUnit TIME_UNIT = TimeUnit.SECONDS;

    private final static BlockingQueue<Runnable> WORK_QUEUE = new LinkedBlockingQueue<>(5);

    private final static ThreadFactory THREAD_FACTORY = new ThreadFactoryBuilder().setNameFormat(ThreadPoolExecutorHolder.class.getName() + "-pool-%d").build();

    private static ExecutorService threadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
            KEEP_ALIVE_TIME, TIME_UNIT,
            WORK_QUEUE, THREAD_FACTORY, new ThreadPoolExecutor.AbortPolicy());

    public static ExecutorService getThreadPoolExecutor() {
        return threadPoolExecutor;
    }
}
