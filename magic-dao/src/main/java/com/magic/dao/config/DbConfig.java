package com.magic.dao.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author Cheng Yufei
 * @create 2019-12-03 17:05
 **/
@Configuration
@MapperScan(value = "com.magic.dao.mapper")
public class DbConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    @Primary
    public DruidDataSource dataSource() {
        return DruidDataSourceBuilder.create().build();
    }
}
