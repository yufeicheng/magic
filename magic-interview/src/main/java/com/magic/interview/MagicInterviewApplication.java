package com.magic.interview;

import cn.anony.boot.annotation.EnableSms;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author
 * @since 1.0
 */
@SpringBootApplication(scanBasePackages = {"com.magic.interview","com.magic.dao"},exclude = {RabbitAutoConfiguration.class})
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableSms
@EnableAsync
//获取配置文件加密盐值
@PropertySource(value = {"file:D:/encrypt.properties"})
//@PropertySource(value = {"file:/data/encrypt.properties"})
public class MagicInterviewApplication {

    @Value("${jasypt.encryptor.password}")
    private String saltValue;

    public static void main(String[] args) {
        new SpringApplicationBuilder(MagicInterviewApplication.class).run(args);
    }

    /**
     *设置jasypt加/解密类
     * @return
     */
    @Bean
    public BasicTextEncryptor basicTextEncryptor() {
        BasicTextEncryptor encryptor = new BasicTextEncryptor();
        encryptor.setPassword(saltValue);
        return encryptor;
    }

    @Bean
    public RedisTemplate getRedisTemplate(RedisTemplate redisTemplate) {

        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(mapper);

        redisTemplate.setDefaultSerializer(serializer);
        redisTemplate.setHashKeySerializer(serializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        //会对value进行设置
        //redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}