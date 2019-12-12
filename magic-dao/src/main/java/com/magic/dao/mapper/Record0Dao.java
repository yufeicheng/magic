package com.magic.dao.mapper;

import com.magic.dao.config.DataSource;
import com.magic.dao.config.DataSourceType;
import com.magic.dao.model.Record0;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface Record0Dao {
    int deleteByPrimaryKey(Long id);

    int insert(Record0 record);

    int insertSelective(Record0 record);

    Record0 selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Record0 record);

    int updateByPrimaryKey(Record0 record);

    @Select("select * from record0")
    @ResultType(Record0.class)
    @DataSource(DataSourceType.TYPE.SECOND)
    List<Record0> getRecord0List();
}