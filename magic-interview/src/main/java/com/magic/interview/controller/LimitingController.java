package com.magic.interview.controller;

import com.magic.interview.service.current_limiting.RateLimitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Cheng Yufei
 * @create 2019-11-05 11:06
 **/
@RestController
@RequestMapping("/limit")
@Validated
@Slf4j
public class LimitingController {

    @Autowired
    private RateLimitService rateLimitService;

    @GetMapping("/breakdown")
    public Object breakdown()  {
        rateLimitService.limit();
        return "success";
    }
}