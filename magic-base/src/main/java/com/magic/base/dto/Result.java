package com.magic.base.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Cheng Yufei
 * @create 2019-11-06 16:47
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    private Integer code;

    private Object data;

    private String msg;

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static Result success() {
        Result result = new Result();
        result.setCode(0);
        result.setMsg("成功");
        return result;
    }

    public static Result success(Object data) {
        Result result = new Result();
        result.setCode(0);
        result.setMsg("成功");
        result.setData(data);
        return result;
    }

    public static Result fail(String msg) {
        Result result = new Result();
        result.setCode(1);
        result.setMsg(msg);
        return result;
    }

    public static Result fail() {
        Result result = new Result();
        result.setCode(1);
        result.setMsg("系统错误");
        return result;
    }
}
