package com.magic.api.service;

import cyf.dubbo.common.interfaces.user.UserService;
import cyf.dubbo.common.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * dubbo引用测试
 *
 * @author Cheng Yufei
 * @create 2019-08-10 22:40
 **/
@Service
@Slf4j
public class UserReferenceService {

   /* @Reference(group = "CityModule", version = "1.0.0", interfaceClass = CityService.class, url = "192.168.99.113:20881")
    private CityService cityService;*/

    @Resource
    private UserService dubboUserService;

    public User getUser() {
        return dubboUserService.getUser();
    }
}
