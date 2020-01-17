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
    public void consume(CommonMessage commonMessage, Message message, Channel channel) throws Exception {

        log.info(">>>接受消息:{}", commonMessage.toString());


        //业务处理
        Thread.sleep(200);
        //throw new Exception();
    }

}
