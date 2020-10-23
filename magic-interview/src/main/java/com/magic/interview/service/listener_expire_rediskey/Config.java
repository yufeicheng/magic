package com.magic.interview.service.listener_expire_rediskey;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 *
 * @author Cheng Yufei
 * @create 2020-10-23 11:45
 **/
@Configuration
public class Config {

	@Bean(name = "redisMessageListenerContainer")
	public RedisMessageListenerContainer container(RedisConnectionFactory factory) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(factory);
		return container;
	}
}
