package com.magic.interview.service.event_listener;

import com.magic.dao.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author Cheng Yufei
 * @create 2020-01-15 11:15
 * 用户注册短信业务
 **/
@Component
@Slf4j
public class RegisterMsgListener {

    //@EventListener
    public void handler(RegisterEvent event) {
        User user = event.getUser();

        //短信处理
        log.info(">>>监听注册事件，短信 处理成功");
    }
}
