package com.magic.rabbit.config;

import com.magic.dao.enums.MsgLogStatus;
import com.magic.dao.mapper.MsgLogMapper;
import com.magic.dao.model.MsgLog;
import com.magic.rabbit.model.CommonMessage;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 处理消息消费时的幂等性、消息状态更新以及消息确认
 * @author Cheng Yufei
 * @create 2020-01-17 10:35
 **/
@Component
@Aspect
@Slf4j
public class AckAopHandler {

    @Autowired
    private MsgLogMapper msgLogMapper;


    @Around(value = "@annotation(rabbitListener)")
    public Object handler(ProceedingJoinPoint pjp, RabbitListener rabbitListener) throws IOException {

        Object proceed = new Object();
        Message message = null;
        Channel channel = null;
        try {
            Object[] args = pjp.getArgs();
            CommonMessage commonMessage = (CommonMessage) args[0];
            message = (Message) args[1];
            channel = (Channel) args[2];

            //幂等
            //eg：未ack消息，再次启动时会重新被消费，但此消息之前已消费，此处保证不在次消费
            String msgId = commonMessage.getMsgId();
            MsgLog msgLog = msgLogMapper.selectByPrimaryKey(msgId);
            if (Objects.isNull(msgLog) || (Objects.nonNull(msgLog) && msgLog.getStatus().equals(MsgLogStatus.SPEND.getCode()))) {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                log.info(">>重复消费");
                return proceed;
            }

            proceed = pjp.proceed();

            MsgLog updateMsg = new MsgLog();
            updateMsg.setMsgId((commonMessage).getMsgId());
            updateMsg.setStatus(MsgLogStatus.SPEND.getCode());
            msgLogMapper.updateByPrimaryKeySelective(updateMsg);
            //不手动确认时，即使消息消费完成，仍会保留在mq中，直至确认；会在服务重启时再次进行投递，此时需要保证幂等性
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            log.info(">>消息已确认，消费成功");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            //业务异常的消息重新入队消费
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
        return proceed;
    }

}
