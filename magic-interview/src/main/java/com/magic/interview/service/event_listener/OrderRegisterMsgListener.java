package com.magic.interview.service.event_listener;

import com.magic.dao.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author Cheng Yufei
 * @create 2020-01-15 11:47
 **/
@Component
@Slf4j
public class OrderRegisterMsgListener implements SmartApplicationListener {
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
        return 0;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        RegisterEvent registerEvent = (RegisterEvent) event;
        User user = registerEvent.getUser();

        //短信处理
        log.info(">>>监听注册事件，短信 处理成功");
    }
}
