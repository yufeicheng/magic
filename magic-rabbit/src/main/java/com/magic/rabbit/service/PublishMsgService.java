package com.magic.rabbit.service;

import com.alibaba.fastjson.JSONObject;
import com.magic.dao.mapper.MsgLogMapper;
import com.magic.dao.model.MsgLog;
import com.magic.dao.model.User;
import com.magic.rabbit.config.Constant;
import com.magic.rabbit.model.CommonMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 发送消息
 *
 * @author Cheng Yufei
 * @create 2020-01-16 10:49
 **/
@Service
@Slf4j
public class PublishMsgService {


    @Autowired
    private MsgLogMapper msgLogMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public String send() {
        String msgId = UUID.randomUUID().toString();

        CommonMessage commonMessage = new CommonMessage();
        commonMessage.setMsgId(msgId);

        User user = new User();
        user.setUsername("green");
        commonMessage.setData(user);

        MsgLog msgLog = new MsgLog();
        msgLog.setMsgId(msgId);
        msgLog.setMsg(JSONObject.toJSONString(commonMessage));
        msgLog.setExchange(Constant.COMMON_EXCHANGE);
        msgLog.setRoutingKey(Constant.COMMON_ROUTING_KEY);
        msgLog.setNextTryTime(Date.from(LocalDateTime.now().plus(2, ChronoUnit.MINUTES).atZone(ZoneId.systemDefault()).toInstant()));
        msgLogMapper.insertSelective(msgLog);

        rabbitTemplate.convertAndSend(Constant.COMMON_EXCHANGE, Constant.COMMON_ROUTING_KEY, commonMessage, new CorrelationData(msgId));

        return "success";
    }
}
