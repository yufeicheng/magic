package com.magic.interview.service.validated;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * //@NonNull  @CleanUp  @Synchronized
 *
 * @author Cheng Yufei
 * @create 2019-11-05 10:32
 **/
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
//自定义时间校验
@MyCheck(startTime = "startTime",endTime = "endTime",message = "结束时间不能早于开始时间")
public class LombokDto implements Serializable {
    //set属性时会做null的校验
    @NonNull
    Integer cid;
    String name;

    String location;

    /**
     * 使用jackjson时
     */
    /*@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    Date time;*/

    /**
     * 使用fastjson时
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    Date time;

    String startTime;

    String endTime;
}
