package com.magic.dao.mapper;

import com.magic.dao.model.MsgLog;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface MsgLogMapper {
    int deleteByPrimaryKey(String msgId);

    int insert(MsgLog record);

    int insertSelective(MsgLog record);

    MsgLog selectByPrimaryKey(String msgId);

    int updateByPrimaryKeySelective(MsgLog record);

    int updateByPrimaryKey(MsgLog record);

    List<MsgLog> selectByTimeout();

    int updateCount(@Param("nextTryTime") Date nextTryTime, @Param("msgId") String msgId);
}