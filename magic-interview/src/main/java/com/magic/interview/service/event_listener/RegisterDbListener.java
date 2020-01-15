package com.magic.interview.service.event_listener;

import com.magic.dao.mapper.UserMapper;
import com.magic.dao.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author Cheng Yufei
 * @create 2020-01-15 11:10
 **/
@Component
@Slf4j
public class RegisterDbListener {

    @Autowired
    private UserMapper userMapper;

    //@EventListener
    @Async
    public void handler(RegisterEvent registerEvent) {

        User user = registerEvent.getUser();

        userMapper.insertSelective(user);
        log.info(">>>监听注册事件，DB 处理成功");
    }
}
