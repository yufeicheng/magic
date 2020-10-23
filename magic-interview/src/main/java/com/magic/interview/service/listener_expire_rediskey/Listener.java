package com.magic.interview.service.listener_expire_rediskey;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * 需在 redis.conf 中设置： notify-keyspace-events Ex
 *
 * 监听redis中过期key，比如：订单30分钟未支付，自动关闭
 * @author Cheng Yufei
 * @create 2020-10-23 11:02
 **/
@Component
@Slf4j
public class Listener extends KeyExpirationEventMessageListener {


	/**
	 * Creates new {@link MessageListener} for {@code __keyevent@*__:expired} messages.
	 *
	 * @param listenerContainer must not be {@literal null}.
	 */
	public Listener(@Qualifier("redisMessageListenerContainer") RedisMessageListenerContainer listenerContainer) {
		super(listenerContainer);
	}

	@Override
	public void onMessage(Message message, byte[] pattern) {
		String expireKey = message.toString();
		log.info(">>>>>过期key：{}", expireKey);
	}
}
