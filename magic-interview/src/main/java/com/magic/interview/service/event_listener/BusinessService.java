package com.magic.interview.service.event_listener;

import com.magic.dao.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * @author Cheng Yufei
 * @create 2020-01-15 11:07
 **/
@Service
@Slf4j
public class BusinessService {

    @Autowired
    private ApplicationContext context;


    public void register(User user) throws InterruptedException {

        //注册逻辑
        Thread.sleep(200);

        //发布事件
        log.info(">>>发布用户注册事件");
        context.publishEvent(new RegisterEvent(this, user));
    }
}
