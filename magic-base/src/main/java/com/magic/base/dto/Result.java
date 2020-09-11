package com.magic.base.dto;

import com.magic.base.dto.enums.RespStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Cheng Yufei
 * @create 2019-11-06 16:47
 **/
@Getter
@Setter
@ToString
public class Result<T> implements Serializable {

	public Result() {
	}

	private static final long serialVersionUID = 290724166702666415L;
	private Integer code;

	private T data;

	private String msg;

	public Result(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public Result(T data) {
		this.code = RespStatusEnum.OK.getStatus();
		this.msg = RespStatusEnum.OK.getDesc();
		this.data = data;
	}

	public static Result success() {
		Result result = new Result();
		result.setCode(0);
		result.setMsg("成功");
		return result;
	}

	public static Result success(Object data) {
		Result result = new Result();
		result.setCode(RespStatusEnum.OK.getStatus());
		result.setMsg(RespStatusEnum.OK.getDesc());
		result.setData(data);
		return result;
	}

	public static Result fail(Integer code, String msg) {
		Result result = new Result();
		result.setCode(code);
		result.setMsg(msg);
		return result;
	}
	public static Result fail(RespStatusEnum statusEnum) {
		Result result = new Result();
		result.setCode(statusEnum.getStatus());
		result.setMsg(statusEnum.getDesc());
		return result;
	}
	public static Result fail() {
		Result result = new Result();
		result.setCode(1);
		result.setMsg("系统错误");
		return result;
	}
}
