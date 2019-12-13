package com.magic.dao.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.google.common.collect.ImmutableMap;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Bean(name = "first")
    @ConfigurationProperties(prefix = "spring.datasource.druid.first")
    @Primary
    public DruidDataSource firstDtaSource() {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        return dataSource;
    }

    @Bean(name = "second")
    @ConfigurationProperties(prefix = "spring.datasource.druid.second")
    public DruidDataSource secondDataSource() {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        return dataSource;
    }

    @Bean(name = "dynamicDataSource")
    public DataSourceRouting dataSource(@Qualifier("first") DataSource first, @Qualifier("second") DataSource second) {
        DataSourceRouting dataSourceRouting = new DataSourceRouting();
        dataSourceRouting.setTargetDataSources(ImmutableMap.of(DataSourceType.Type.FIRST, first, DataSourceType.Type.SECOND, second));
        dataSourceRouting.setDefaultTargetDataSource(first);
        return dataSourceRouting;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dynamicDataSource") DataSource dynamicDataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dynamicDataSource);
        return factoryBean.getObject();
    }
}
