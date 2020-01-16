package com.magic.rabbit.timer;

import com.alibaba.fastjson.JSONObject;
import com.magic.dao.enums.MsgLogStatus;
import com.magic.dao.mapper.MsgLogMapper;
import com.magic.dao.model.MsgLog;
import com.magic.rabbit.config.Constant;
import com.magic.rabbit.model.CommonMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Cheng Yufei
 * @create 2020-01-16 11:35
 **/
@Component
@Slf4j
public class ReSendTimer {

    @Autowired
    private MsgLogMapper msgLogMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    private int maxTryCount = 3;

    @Scheduled(cron = "0/30 * * * * ?")
    public void reSend() {
        List<MsgLog> msgLogs = msgLogMapper.selectByTimeout();
        if (msgLogs.isEmpty()) {
            log.info(">>无重新投递数据");
            return;
        }

        msgLogs.forEach(m -> {
            if (m.getTryCount() >= maxTryCount) {
                log.info(">>超过最大投递次数，投递失败，msgId：{}", m.getMsgId());
                MsgLog msgLog = new MsgLog();
                msgLog.setMsgId(m.getMsgId());
                msgLog.setStatus(MsgLogStatus.DELIVER_FAIL.getCode());
                msgLogMapper.updateByPrimaryKeySelective(msgLog);
                return;
            }
            msgLogMapper.updateCount(m.getNextTryTime(), m.getMsgId());
            rabbitTemplate.convertAndSend(Constant.COMMON_EXCHANGE, Constant.COMMON_ROUTING_KEY, JSONObject.parseObject(m.getMsg(), CommonMessage.class), new CorrelationData(m.getMsgId()));

            log.info(">>msgId:{},第{}次已投递", m.getMsgId(), m.getTryCount() + 1);
        });

    }
}
