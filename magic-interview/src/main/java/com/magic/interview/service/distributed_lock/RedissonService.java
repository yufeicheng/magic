package com.magic.interview.service.distributed_lock;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * @author Cheng Yufei
 * @create 2020-01-08 17:01
 **/
@Service
@Slf4j
public class RedissonService {

    @Autowired
    private RedissonClient redissonClient;

    private int num = 50;

    private RLock rLock;

    @PostConstruct
    public RLock getClient() {
        rLock = redissonClient.getLock("my_lock");
        return rLock;
    }

    public void handler() throws InterruptedException {

        if (num <= 0) {
            log.info("已抢完");
            return;
        }
        //等待锁定时间不超过4秒，锁过期时间10秒。
        // 业务需40秒但锁10秒已失效，业务没执行完，此时再有请求时，会有线程安全问题，unlock时会报 IllegalMonitorStateException 异常【因为锁失效已经匹配不上了】。
        //boolean lock = rLock.tryLock(4, 10, TimeUnit.SECONDS);

        //最大等待锁时间6秒，时间过期返回false；
        // 此时没有锁过期时间，redisson默认会设置为30s，watchdog每10秒检测key是否存在，若存在会重设锁过期时间为30s，保证业务执行时间过长时，锁已经失效的问题；
        //采用此方式
        boolean lock = rLock.tryLock(6, TimeUnit.SECONDS);

        //lock && num >0 条件分开，避免获得锁后 因为num =0 而锁没有执行unlock，导致watchdog不停重设锁过期时间30s，锁得不到释放
        if (lock) {
            try {
                if (num > 0) {

                    log.info(">>获得数字-- ：{}", num);
                    //业务执行需40s,使用不设过期时间的tryLock时，会默认30s过期，watchdog会不断延时锁过期时间
                    //Thread.sleep(40000);
                    Thread.sleep(100);
                    num--;
                }

            } finally {
                log.info("解锁");
                rLock.unlock();
            }
        } else {
            log.info(">>未获得");
        }

    }

    /**
     * jmeter: 50个资源，150个线程0.5秒内请求，会出现>50显示获得数字
     */
    public void noLock() {
        if (num <= 0) {
            log.info("已抢完");
            return;
        }

        log.info(">>获得数字-- ：{}", num);
        num--;
        /*for (int i = 0; i < 20; i++) {

            new Thread(()->{
                num--;
                log.info(">>获得数字-- ：{}",num);
            }).start();
        }*/


    }

    public void reSet(Integer num) {
        this.num = num;
    }
}
