package com.magic.dao.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 数据源路由
 *
 * @author Cheng Yufei
 * @create 2019-12-12 17:21
 **/
public class DataSourceRouting extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceType.threadLocal.get();
    }
}
