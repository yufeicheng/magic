package com.magic.interview.service.parameter_annotation;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author Cheng Yufei
 * @create 2020-01-14 15:23
 **/
@Component
public class MyApplicationContext implements ApplicationContextAware {


    private static ApplicationContext context;

    public static  <T> T getBean(Class<T> requiredType) {
        return context.getBean(requiredType);
    }

    /**
     * 工程启动时执行此方法
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        MyApplicationContext.context = applicationContext;
    }
}
