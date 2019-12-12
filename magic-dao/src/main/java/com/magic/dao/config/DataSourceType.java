package com.magic.dao.config;

import lombok.Getter;

/**
 * 数据源类型设置
 *
 * @author Cheng Yufei
 * @create 2019-12-12 15:56
 **/
@Getter
public class DataSourceType {

   public enum TYPE {
        //
        FIRST,
        SECOND

    }

   protected static final ThreadLocal<TYPE> threadLocal = new ThreadLocal<>();
}
