package com.magic.api.configuration;

import com.alibaba.dubbo.config.annotation.Reference;
import cyf.dubbo.common.interfaces.city.CityService;
import cyf.dubbo.common.interfaces.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Cheng Yufei
 * @create 2019-08-12 10:13
 **/
@Configuration
public class ReferenceConfiguration {

    @Reference(group = "CityModule", version = "1.0.0", interfaceClass = CityService.class/*, url = "10.66.5.170:20881"*/)
    private CityService dubboCityService;
    @Reference(group = "UserModule", version = "1.0.0", interfaceClass = UserService.class/*, url = "10.66.5.170:20880"*/)
    private UserService dubboUserService;

    @Bean
    public CityService getDubboCityService() {
        return dubboCityService;
    }

    @Bean
    public UserService getDubboUserService() {
        return dubboUserService;
    }
}
