package com.springboot.provider.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@EnableScheduling
public class ScheduleTask {

    private final Logger logger = LoggerFactory.getLogger(ScheduleTask.class);

    @Scheduled(cron = "0 0/30 * * * ?") //间隔30分钟
    public void unReadMessageTask() {
        logger.info(String.format("启动监控服务: %s", Thread.currentThread().getName()));
        logger.info(String.format("最大内存: %d M, 已分配内存: %d M, 已分配内存中剩余空间: %d M, 最大可用内存: %d M",
                Runtime.getRuntime().maxMemory() / 1024 / 1024,
                Runtime.getRuntime().maxMemory() / 1024 / 1024,
                Runtime.getRuntime().maxMemory() / 1024 / 1024,
                (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory()) / 1024 / 1024
        ));
    }
}
