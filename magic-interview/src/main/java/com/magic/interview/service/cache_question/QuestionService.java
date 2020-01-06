package com.magic.interview.service.cache_question;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.magic.dao.mapper.Record0Dao;
import com.magic.dao.model.Record0;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.Collator;
import java.time.temporal.ValueRange;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * 缓存相关问题
 *
 * @author Cheng Yufei
 * @create 2020-01-03 10:35
 **/
@Service
@Slf4j
public class QuestionService {

    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private Record0Dao record0Dao;

    public static final String dataKey = "record:list";
    public static final String bdKey = "breakdown:";
    public static final int bdValue = 1;

    /**
     * 缓存雪崩：
     *      1. 同一时间，大量缓存失效，导致请求到达db
     *      2. 解决： 可将缓存失效时间，在1-5分钟内进行随机
     */


    /**
     * 缓存击穿：
     * 1. 缓存失效的同时，有大量请求落到数据库问题
     *
     * 解决：
     *  方案一： 利用redis 操作有成功返回值的命令来实现简单的锁，返回成功是去操作数据库，load缓存，其余请求等待缓存设置。【eg： breakdown()】
     *
     *  方案二： 数据缓存永不过期，异步线程刷新缓存。
     *
     *  方案三： hystrix 降级
     */
    public Object breakdown() throws InterruptedException {
        ValueOperations operations = redisTemplate.opsForValue();
        Object res = operations.get(dataKey);
        if (Objects.nonNull(res)) {
            return JSONObject.toJSONString(res);
        }

        //key值不存在时进行设置，返回true
        Boolean ifAbsent = operations.setIfAbsent(bdKey, bdValue, 2, TimeUnit.MINUTES);

        //说明此时已有请求去操作数据库，等待获取缓存即可
        if (!ifAbsent) {
            Thread.sleep(30);
            return JSONObject.toJSONString(operations.get(dataKey));
        }

        //已获得”锁“，请求数据库，load缓存
        List<Record0> record0List = record0Dao.getRecord0List();
        log.info(">>>缓存失效，操作数据库<<<");
        operations.set(dataKey, record0List, 3, TimeUnit.MINUTES);
        redisTemplate.delete(bdKey);
        return record0List;

        //key值存在设置过期时间，返回true，否则不做操作
       /* Boolean present = operations.setIfPresent(bdKey, bdValue, 3, TimeUnit.MINUTES);
        System.out.println(present);*/
    }

    public String set(String type, String key, String value) {
        String res = "";
        switch (type) {
            case "set":
                redisTemplate.opsForValue().set(key, value);
                res = "success";
                break;
            case "get":
                Object o = redisTemplate.opsForValue().get(key);
                res = JSON.toJSONString(o);
                break;
            case "del":
                redisTemplate.delete(key);
            default:
                break;
        }
        return res;
    }
}
