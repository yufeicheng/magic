package com.magic.interview.service.validated;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.Delegate;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * validated校验@RequestBody List<DTO> 时，直接使用List，不会走校验，需要自定义
 *31051700701502715133172
 * @author Cheng Yufei
 * @create 2020-09-04 11:20
 **/
@NoArgsConstructor
@AllArgsConstructor
public class ValidationList<T> implements List<T>{


	@Delegate
	@Valid
	public List<T> list = new ArrayList<>();

	@Override
	public String toString() {
		return "ValidationList{" +
				"list=" + list.toString() +
				'}';
	}
}
