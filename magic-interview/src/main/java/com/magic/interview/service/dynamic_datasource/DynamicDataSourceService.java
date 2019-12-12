package com.magic.interview.service.dynamic_datasource;

import com.magic.dao.mapper.Record0Dao;
import com.magic.dao.model.Record0;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Cheng Yufei
 * @create 2019-12-03 15:44
 **/
@Service
@Slf4j
public class DynamicDataSourceService {

    @Resource
    private Record0Dao record0Dao;


    public List<Record0> getList() {
        return record0Dao.getRecord0List();
    }
}
