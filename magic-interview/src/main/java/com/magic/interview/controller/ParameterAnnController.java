package com.magic.interview.controller;

import com.magic.dao.mapper.UserMapper;
import com.magic.dao.model.Record0;
import com.magic.dao.model.User;
import com.magic.interview.service.encrypt_properties.EncryptService;
import com.magic.interview.service.parameter_annotation.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Cheng Yufei
 * @create 2019-12-03 17:30
 **/
@RestController
@RequestMapping("/parameter")
public class ParameterAnnController {

    @Resource
    private EncryptService encryptService;

    @GetMapping("/anno")
    public List<User> getUsers(@CurrentUser Record0 user) {

        //System.out.println(user.getUsername());
        return encryptService.getUsers();
    }

}
