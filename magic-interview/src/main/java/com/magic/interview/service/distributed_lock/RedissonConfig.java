package com.magic.interview.service.distributed_lock;

import org.jasypt.util.text.BasicTextEncryptor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Cheng Yufei
 * @create 2020-01-08 15:46
 **/
@Configuration
public class RedissonConfig {


    @Autowired
    private BasicTextEncryptor basicTextEncryptor;
    @Value("${spring.redis.password}")
    private String redisPwd;
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private Integer port;

    /**
     * doc: https://github.com/redisson/redisson/wiki/%E7%9B%AE%E5%BD%95
     *
     * @return
     */
    @Bean
    public RedissonClient init() {
        Config config = new Config();
        //可设置cluster 或者 sentinel 模式
        config.useSingleServer().setAddress("redis://"+host + ":" + port).setPassword(redisPwd);
        return Redisson.create(config);

    }
}
