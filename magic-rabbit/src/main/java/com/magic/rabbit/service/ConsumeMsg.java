package com.magic.rabbit.service;

import com.magic.dao.enums.MsgLogStatus;
import com.magic.dao.mapper.MsgLogMapper;
import com.magic.dao.model.MsgLog;
import com.magic.rabbit.config.Constant;
import com.magic.rabbit.model.CommonMessage;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

/**
 * @author Cheng Yufei
 * @create 2020-01-16 11:03
 **/
@Component
@Slf4j
public class ConsumeMsg {


    @Autowired
    private MsgLogMapper msgLogMapper;

    @RabbitListener(queues = {Constant.COMMON_QUEUE})
    public void consume(CommonMessage commonMessage, Message message, Channel channel) throws IOException {

        try {
            log.info(">>>接受消息:{}", commonMessage.toString());

            //幂等
            //eg：未ack消息，再次启动时会重新被消费，但此消息之前已消费，此处保证不在次消费
            String msgId = commonMessage.getMsgId();
            MsgLog msgLog = msgLogMapper.selectByPrimaryKey(msgId);
            if (Objects.isNull(msgLog) || (Objects.nonNull(msgLog) && msgLog.getStatus().equals(MsgLogStatus.SPEND.getCode()))) {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                log.info(">>重复消费");
                return;
            }

            //业务处理
            Thread.sleep(200);
            //throw new Exception();

            MsgLog updateMsg = new MsgLog();
            updateMsg.setMsgId(msgId);
            updateMsg.setStatus(MsgLogStatus.SPEND.getCode());
            msgLogMapper.updateByPrimaryKeySelective(updateMsg);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        } catch (Exception e) {
            //重新入队消费
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }


    }

}
