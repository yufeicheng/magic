package com.magic.rabbit.config;

import com.magic.dao.enums.MsgLogStatus;
import com.magic.dao.mapper.MsgLogMapper;
import com.magic.dao.model.MsgLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * \
 *
 * @author Cheng Yufei
 * @create 2020-01-16 10:09
 **/
@Configuration
@Slf4j
public class MqConfig {

    @Autowired
    private CachingConnectionFactory cachingConnectionFactory;

    @Autowired
    private MsgLogMapper msgLogMapper;


    /**
     * 定义转换后，可在 @RabbitListener 方法中接收 自定义的消息类CommonMessage
     * @return
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate initRabbitTemplate(@Qualifier("messageConverter") MessageConverter converter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        rabbitTemplate.setMessageConverter(converter);

        //消息是否成功发送到Exchange
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                log.info(">>>消息成功发送到Exchange");
                String msgId = correlationData.getId();
                //更新消息状态
                MsgLog msgLog = new MsgLog();
                msgLog.setMsgId(msgId);
                //投递成功
                msgLog.setStatus(MsgLogStatus.DELIVER_SUCCESS.getCode());
                //msgLogMapper.updateByPrimaryKeySelective(msgLog);
            } else {
                log.info("消息发送到Exchange失败, {}, cause: {}", correlationData, cause);
            }
        });

        // 触发setReturnCallback回调必须设置mandatory=true, 否则Exchange没有找到Queue就会丢弃掉消息, 而不会触发回调
        rabbitTemplate.setMandatory(true);

        // 消息是否从Exchange路由到Queue, 注意: 这是一个失败回调, 只有消息从Exchange路由到Queue失败才会回调这个方法
        rabbitTemplate.setReturnCallback((message, replyCode, replyText,
                                          exchange, routingKey) -> {

            log.info("消息从Exchange路由到Queue失败: exchange: {}, route: {}, replyCode: {}, replyText: {}, message: {}", exchange, routingKey, replyCode, replyText, message);
        });
        return rabbitTemplate;
    }

    @Autowired
    public void init(AmqpAdmin amqpAdmin) {
        Exchange common_exchange = ExchangeBuilder.directExchange(Constant.COMMON_EXCHANGE).durable(true).build();

        Queue common_queue = QueueBuilder.durable(Constant.COMMON_QUEUE).build();

        Binding binding = BindingBuilder.bind(common_queue).to(common_exchange).with(Constant.COMMON_ROUTING_KEY).noargs();

        amqpAdmin.declareExchange(common_exchange);
        amqpAdmin.declareQueue(common_queue);
        amqpAdmin.declareBinding(binding);

    }

}
