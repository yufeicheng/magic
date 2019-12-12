package com.magic.dao.mapper;

import com.magic.dao.config.DataSource;
import com.magic.dao.config.DataSourceType;
import com.magic.dao.model.User;
import com.magic.dao.model.UserExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    long countByExample(UserExample example);

    int deleteByExample(UserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    int selfPlusMinus(@Param("columnName") String columnName, @Param("operator") String operator, @Param("count") int count, @Param("example") Object example);

    List<User> selectByExample(UserExample example);

    User selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

    int updateByExample(@Param("record") User record, @Param("example") UserExample example);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     *  @ResultType 可返回自定义DTO，通过 select username as name 来对应dto中自定义的属性值
     * @return
     */
    @Select("select * from user")
    @ResultType(User.class)
    @DataSource(DataSourceType.TYPE.FIRST)
    List<User> getUserList();
}