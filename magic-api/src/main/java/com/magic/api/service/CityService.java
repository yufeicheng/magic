package com.magic.api.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.magic.base.util.JSONToolKit;
import com.magic.interfaces.service.MagicCityService;
import cyf.dubbo.common.model.City;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadLocalRandom;

/**
 * dubbo 服务提供测试
 *
 * @author Cheng Yufei
 * @create 2019-08-11 11:18
 **/
@Service(version = "1.0.0", group = "MagicCityService", interfaceClass = MagicCityService.class)
@Slf4j
public class CityService implements MagicCityService {

    @Override
    public JSONObject cityHandle(Integer id) {
        City city = new City();
        city.setId(id);
        city.setCityName(String.valueOf(ThreadLocalRandom.current().nextDouble()));
        log.debug("请求参数：{} ,结果：{}", id, JSON.toJSON(city));
        return JSONToolKit.success(city);
    }
}
