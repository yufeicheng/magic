package com.magic.dao.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

/**
 * 数据源设置
 *
 * @author Cheng Yufei
 * @create 2019-12-12 15:50
 **/
@Aspect
@Slf4j
@Component
public class AopDataSource {

    @Before("@annotation(dataSource)")
    public void dataSourceInfo(DataSource dataSource) {
        DataSourceType.TYPE value = dataSource.value();
        DataSourceType.threadLocal.set(value);
    }

    @After("@annotation(com.magic.dao.config.DataSource)")
    public void remove() {
        DataSourceType.threadLocal.remove();
    }
}
