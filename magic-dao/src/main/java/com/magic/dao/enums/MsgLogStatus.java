package com.magic.dao.enums;

import lombok.Getter;

/**
 * @author Cheng Yufei
 * @create 2020-01-16 15:04
 **/
public enum MsgLogStatus {

    //
    DELIVERING(0,"投递中"),
    DELIVER_SUCCESS(1,"投递成功"),
    DELIVER_FAIL(2,"投递失败"),
    SPEND(3,"已消费"),
    ;

    @Getter
    private Integer code;
    private String desc;

    MsgLogStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
