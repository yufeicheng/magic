package com.magic.interview.service.current_limiting;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * guava 限流
 *
 * @author Cheng Yufei
 * @create 2020-01-08 9:30
 **/
@Service
@Slf4j
public class RateLimitService {

    private RateLimiter rateLimiter;

   /* @PostConstruct
    public void init() {
        //每秒产生2个令牌
        //rateLimiter = RateLimiter.create(2);

        //设置缓冲时间为3秒，刚开始不会以每0.5秒速率产生令牌，形成平滑下降坡度，缓冲时间内达到固定频率输出
        rateLimiter = RateLimiter.create(2,2, TimeUnit.SECONDS);
    }


    public void limit() {

        //申请1个令牌，阻塞直到申请成功，返回所等待的时间
        double acquire = rateLimiter.acquire(1);

        //log.info(String.valueOf(acquire));

    }*/

    //private static RateLimiter smoothBursty = RateLimiter.create(10);
    private static RateLimiter smoothWarmingUp = RateLimiter.create(2,1,TimeUnit.SECONDS);

    public static void main(String[] args) {
        Stream.iterate(1, k -> ++k).limit(10).forEach(s -> {
            double acquire = smoothWarmingUp.acquire();
            System.out.println(acquire);
        });
    }


}
