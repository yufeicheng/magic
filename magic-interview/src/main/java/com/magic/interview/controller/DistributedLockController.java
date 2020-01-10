package com.magic.interview.controller;

import com.magic.interview.service.distributed_lock.RedissonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Cheng Yufei
 * @create 2019-11-05 11:06
 **/
@RestController
@RequestMapping("/lock")
@Validated
@Slf4j
public class DistributedLockController {

    @Autowired
    private RedissonService redissonService;

    @GetMapping("/redisson")
    public String redisson() throws InterruptedException {
        redissonService.handler();
        return "success";
    }
    @GetMapping("/noLock")
    public String noLock() {
        redissonService.noLock();
        return "success";
    }

    @GetMapping("/reSet/{num}")
    public String reSet(@PathVariable Integer num) {

        redissonService.reSet(num);
        return "success";
    }
}