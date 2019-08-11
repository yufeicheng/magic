package com.magic.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.magic.api.service.CityReferenceService;
import com.magic.base.util.JSONToolKit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Cheng Yufei
 * @create 2019-08-10 23:12
 **/
@RestController
@RequestMapping("/dubbo")
public class DubboController {


    @Resource
    private CityReferenceService cityReferenceService;

    @GetMapping("/getCityName")
    public JSONObject getCityName(@RequestParam String cityName) {
        return JSONToolKit.success(cityReferenceService.getCityName(cityName));
    }

}
