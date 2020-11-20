package com.magic.interview.service.redis_timeseries;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.magic.interview.service.pagehelper.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.Kernel;

/**
 * @author Cheng Yufei
 * @create 2020-09-09 18:14
 **/
@RestController
@RequestMapping("/timeseries")
public class TImeSeriesController {

    @Autowired
    private RedisTimeSeriesService timeSeriesService;


    @GetMapping(value = "/create", produces = "application/json;charset=UTF-8")
    public boolean create(@RequestParam String keySuffix, @RequestParam long retention, @RequestParam String labelValue) {
        return timeSeriesService.create_key(keySuffix, retention, labelValue);
    }

    @GetMapping(value = "/add", produces = "application/json;charset=UTF-8")
    public long add(@RequestParam String key, @RequestParam Integer value) {
        return timeSeriesService.add(key, value);
    }

    @GetMapping(value = "/get", produces = "application/json;charset=UTF-8")
    public Object get(@RequestParam String type, @RequestParam String key) {
        return timeSeriesService.get(type, key);
    }

    @GetMapping(value = "/range", produces = "application/json;charset=UTF-8")
    public Object range(@RequestParam String key, @RequestParam int count) {
        timeSeriesService.range(key, 1605780090L, 1605780302L, count);
        return "success";
    }

}
