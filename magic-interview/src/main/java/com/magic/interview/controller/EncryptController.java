package com.magic.interview.controller;

import com.magic.dao.model.User;
import com.magic.interview.service.encrypt_properties.EncryptService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Cheng Yufei
 * @create 2019-12-03 17:30
 **/
@RestController
@RequestMapping("/encry")
public class EncryptController {

    @Resource
    private EncryptService encryptService;

    @GetMapping("/getUsers")
    public List<User> getUsers() {
        return encryptService.getUsers();
    }

    @GetMapping("/encryptInfo")
    public Map encryptInfo() {
        return encryptService.encryProperties();
    }
}
