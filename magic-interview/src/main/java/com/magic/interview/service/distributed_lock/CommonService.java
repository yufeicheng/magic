package com.magic.interview.service.distributed_lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author Cheng Yufei
 * @create 2020-07-30 17:50
 **/
@Service
@Slf4j
public class CommonService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 分布式锁：
     *      lua 进行key-value对应的原子性删除。
     *      情况：A持有锁，但执行事件过长，超过锁的有效期。此时B申请到锁，正在执行业务。A执行完进行锁删除操作，把B的锁删除了，那么C就又能获取到锁了。
     *      所以在进行锁删除的时候需要看此时锁对应的value是否是自己持有锁时设置的value值，如果不对应，则不能进行删除。
     */
    public void unLock() {
        String command = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        //String luaScript = "local in = ARGV[1] local curr=redis.call('get', KEYS[1]) if in==curr then redis.call('del', KEYS[1]) end return 'OK'"";
        redisTemplate.execute(RedisScript.of(command), Collections.singletonList("key"), Collections.singletonList("value"));
    }

    public void lock() {
        Boolean ifAbsent = redisTemplate.opsForValue().setIfAbsent("key", "value", 20, TimeUnit.SECONDS);
        if (ifAbsent) {
            log.info("获取到锁");
        }
    }
}
