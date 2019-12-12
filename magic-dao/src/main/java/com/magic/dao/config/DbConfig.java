package com.magic.dao.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.google.common.collect.ImmutableMap;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @author Cheng Yufei
 * @create 2019-12-03 17:05
 **/
@Configuration
@MapperScan(value = "com.magic.dao.mapper")
public class DbConfig {

    @Bean("first")
    @ConfigurationProperties(prefix = "spring.datasource.druid.first")
    @Primary
    public DruidDataSource firstDtaSource() {

        return DruidDataSourceBuilder.create().build();
    }

    @Bean("second")
    @ConfigurationProperties(prefix = "spring.datasource.druid.second")
    public DruidDataSource secondDataSource() {

        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    public DataSourceRouting dataSource(DataSource first, DataSource second) {
        DataSourceRouting dataSourceRouting = new DataSourceRouting();
        dataSourceRouting.setTargetDataSources(ImmutableMap.of(DataSourceType.TYPE.FIRST, first, DataSourceType.TYPE.SECOND, second));
        dataSourceRouting.setDefaultTargetDataSource(first);
        return dataSourceRouting;
    }
}
