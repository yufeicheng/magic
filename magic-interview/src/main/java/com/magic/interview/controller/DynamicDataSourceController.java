package com.magic.interview.controller;

import com.magic.dao.model.Record0;
import com.magic.interview.service.dynamic_datasource.DynamicDataSourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Cheng Yufei
 * @create 2019-12-03 17:30
 **/
@RestController
@RequestMapping("/dynamic")
@Slf4j
public class DynamicDataSourceController {

    @Resource
    private DynamicDataSourceService dynamicDataSourceService;

    @GetMapping("/getList")
    public List<Record0> getList() {
        log.info(">>>>docker,hello");
        return dynamicDataSourceService.getList();
    }

}
