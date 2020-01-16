package com.magic.rabbit.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * 投送/接受消息组装对象
 * @author Cheng Yufei
 * @create 2020-01-16 11:14
 **/
@Getter
@Setter
@ToString
public class CommonMessage {

    String msgId;
    Object data;
}
