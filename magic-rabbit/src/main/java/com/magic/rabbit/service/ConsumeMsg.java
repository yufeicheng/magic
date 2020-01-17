package com.magic.rabbit.service;

import com.magic.rabbit.config.Constant;
import com.magic.rabbit.model.CommonMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author Cheng Yufei
 * @create 2020-01-16 11:03
 **/
@Component
@Slf4j
public class ConsumeMsg {


    @RabbitListener(queues = {Constant.COMMON_QUEUE})
    public void consume(CommonMessage commonMessage) throws Exception {

        log.info(">>>接受消息:{}", commonMessage.toString());


        //业务处理
        Thread.sleep(200);
        //throw new Exception();
    }

}
