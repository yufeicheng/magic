package com.magic.api.service;

import com.alibaba.dubbo.config.annotation.Reference;
import cyf.dubbo.common.interfaces.city.CityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * dubbo引用测试
 *
 * @author Cheng Yufei
 * @create 2019-08-10 22:40
 **/
@Service
@Slf4j
public class CityReferenceService {

    @Reference(group = "CityModule", version = "1.0.0", interfaceClass = CityService.class, url = "192.168.99.113:20881")
    private CityService cityService;

    public String getCityName(String cityName) {
        return cityService.getCityName(cityName);
    }

}
