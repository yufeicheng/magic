package com.magic.base.dto.enums;

import lombok.Getter;

@Getter
public enum RespStatusEnum {
	//
	OK(0, "success"),
	ERROR(1000, "系统正忙，请稍后再试"),
	PARAMETER_ERROR(2000, "参数错误"),
	NOT_LOGIN(3000, "未登录"),
	;

	private int status;
	private String desc;

	RespStatusEnum(int status, String desc) {
		this.status = status;
		this.desc = desc;
	}

	public static RespStatusEnum get(Integer status) {
		if (status == null) {
			return null;
		}
		for (RespStatusEnum respStatusEnum : RespStatusEnum.values()) {
			if (respStatusEnum.getStatus() == status) {
				return respStatusEnum;
			}
		}
		return null;
	}
}
