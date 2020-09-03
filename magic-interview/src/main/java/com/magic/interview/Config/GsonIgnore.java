package com.magic.interview.Config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * gson序列化忽略字段
 *
 * @author Cheng Yufei
 * @create 2020-09-03 15:55
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD})
public @interface GsonIgnore {
}
