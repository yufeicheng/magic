package com.magic.interview.service.event_listener;

import com.magic.dao.mapper.UserMapper;
import com.magic.dao.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 顺序执行Listener
 *
 * @author Cheng Yufei
 * @create 2020-01-15 11:46
 **/
@Component
@Slf4j
public class OrderRegisterDbListener implements SmartApplicationListener {

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return eventType.isAssignableFrom(RegisterEvent.class);
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return sourceType.isAssignableFrom(BusinessService.class);
    }

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    @Async
    public void onApplicationEvent(ApplicationEvent event) {
        RegisterEvent registerEvent = (RegisterEvent) event;
        User user = registerEvent.getUser();

        userMapper.insertSelective(user);
        log.info(">>>监听注册事件，DB 处理成功");
    }
}
