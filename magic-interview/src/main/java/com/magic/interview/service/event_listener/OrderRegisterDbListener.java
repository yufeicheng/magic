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

    /**
     * 接受到的监听事件类型的匹配
     * @param eventType
     * @return
     */
    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return eventType.isAssignableFrom(RegisterEvent.class);
    }

    /**
     * 事件发布者类匹配
     * @param sourceType
     * @return
     */
    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return sourceType.isAssignableFrom(BusinessService.class);
    }

    /**
     * 监听执行顺序
     * @return
     */
    @Override
    public int getOrder() {
        return 1;
    }

    /**
     *  supportsEventType && supportsSourceType 时才会执行
     * @param event
     */
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        RegisterEvent registerEvent = (RegisterEvent) event;
        User user = registerEvent.getUser();

        userMapper.insertSelective(user);
        log.info(">>>监听注册事件，DB 处理成功");
    }
}
