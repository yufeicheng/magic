package com.magic.interview.Config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Async异步线程池
 * @author Cheng Yufei
 * @create 2020-01-15 11:35
 **/
@Configuration
public class ThreadPoolConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {

        ThreadPoolTaskExecutor poolTaskExecutor = new ThreadPoolTaskExecutor();
        poolTaskExecutor.setCorePoolSize(5);
        poolTaskExecutor.setMaxPoolSize(30);
        poolTaskExecutor.setKeepAliveSeconds(30);
        poolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        poolTaskExecutor.setQueueCapacity(100);
        poolTaskExecutor.setThreadFactory(new ThreadFactoryBuilder().setNameFormat("task_demo_pool_%d").build());
        poolTaskExecutor.initialize();
        return poolTaskExecutor;
    }
}
