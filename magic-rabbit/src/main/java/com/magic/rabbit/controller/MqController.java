package com.magic.rabbit.controller;

import com.magic.rabbit.service.PublishMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Cheng Yufei
 * @create 2019-11-05 11:06
 **/
@RestController
@RequestMapping("/mq")
public class MqController {


    @Autowired
    private PublishMsgService publishMsgService;

    @GetMapping("/send")
    public String send()  {
        return publishMsgService.send();
    }

}