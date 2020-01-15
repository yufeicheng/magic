package com.magic.interview.controller;

import com.magic.dao.model.User;
import com.magic.interview.service.event_listener.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Cheng Yufei
 * @create 2020-01-15 11:19
 **/
@RestController
@RequestMapping("/event")
public class EventListenerController {

    @Autowired
    private BusinessService businessService;

    @GetMapping("/register")
    public String handler(@RequestBody User user) throws InterruptedException {
        businessService.register(user);
        return "success";
    }
}
