package com.magic.interview.service.redis_timeseries;

import com.google.common.collect.ImmutableMap;
import com.redislabs.redistimeseries.Aggregation;
import com.redislabs.redistimeseries.Range;
import com.redislabs.redistimeseries.RedisTimeSeries;
import com.redislabs.redistimeseries.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;
import java.util.Timer;

/**
 *
 * @author Cheng Yufei
 * @create 2020-11-19 3:32 下午
 **/
@Service
@Slf4j
public class RedisTimeSeriesService {

    private RedisTimeSeries timeSeries;

    @PostConstruct
    public void init() {
        try {
            timeSeries = new RedisTimeSeries("127.0.0.1", 6379);
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info(">>创建 RedisTimeSeries << ");
    }

    public boolean create_key(String keySuffix, long retention, String labelValue) {

        String key = "device:temperature:" + keySuffix;
        boolean create_key = timeSeries.create(key, retention, (Map) ImmutableMap.of("device_id", labelValue));
        return create_key;
    }

    public long add(String key, double value) {
        long add = timeSeries.add(key, Instant.now().getEpochSecond(), value);
        return add;
    }

    public Object get(String type, String key) {
        switch (type) {
            case "get":
                Value value = timeSeries.get(key);
                return value;
            case "mget":
                //!= 判断报错: please provide at least one matcher
                Range[] mget = timeSeries.mget(true, "device_id!=2");
                System.out.println();
                break;
            default:
                break;
        }
        return null;
    }

    /**
     * 获取时间范围内的某个key的指定数量的数据：range(key, from, to, count);
     * 获取时间范围内的某个key指定数量的聚合数据：Aggregation
     *
     * @param key
     * @param from
     * @param to
     * @param count
     */
    public void range(String key , long from , long to,int count ) {
        Value[] range = timeSeries.range(key, from, to, Aggregation.MAX,180000,4);
        //Value[] range = timeSeries.range(key, from, to, count);
        System.out.println();
    }
}
