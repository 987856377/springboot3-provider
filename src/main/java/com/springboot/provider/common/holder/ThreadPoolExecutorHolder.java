package com.springboot.provider.common.holder;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * @Description 第一种拒绝策略是 AbortPolicy，这种拒绝策略在拒绝任务时，会直接抛出异常 RejectedExecutionException （属于RuntimeException），让你感知到任务被拒绝了，于是你便可以根据业务逻辑选择重试或者放弃提交等策略。
 * 第二种拒绝策略是 DiscardPolicy，这种拒绝策略正如它的名字所描述的一样，当新任务被提交后直接被丢弃掉，也不会给你任何的通知，相对而言存在一定的风险，因为我们提交的时候根本不知道这个任务会被丢弃，可能造成数据丢失。
 * 第三种拒绝策略是 DiscardOldestPolicy，如果线程池没被关闭且没有能力执行，则会丢弃任务队列中的头结点，通常是存活时间最长的任务，这种策略与第二种不同之处在于它丢弃的不是最新提交的，而是队列中存活时间最长的，这样就可以腾出空间给新提交的任务，但同理它也存在一定的数据丢失风险。
 * 第四种拒绝策略是 CallerRunsPolicy，相对而言它就比较完善了，当有新任务提交后，如果线程池没被关闭且没有能力执行，则把这个任务交于提交任务的线程执行，也就是谁提交任务，谁就负责执行任务。这样做主要有两点好处。
 * 第一点新提交的任务不会被丢弃，这样也就不会造成业务损失。
 * 第二点好处是，由于谁提交任务谁就要负责执行任务，这样提交任务的线程就得负责执行任务，而执行任务又是比较耗时的，在这段期间，提交任务的线程被占用，也就不会再提交新的任务，减缓了任务提交的速度，相当于是一个负反馈。在此期间，线程池中的线程也可以充分利用这段时间来执行掉一部分任务，腾出一定的空间，相当于是给了线程池一定的缓冲期。
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
            WORK_QUEUE, THREAD_FACTORY, new ThreadPoolExecutor.CallerRunsPolicy());

    public static ExecutorService getThreadPoolExecutor() {
        return threadPoolExecutor;
    }
}
