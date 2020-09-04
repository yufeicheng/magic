package com.magic.interview.service.validated;

import lombok.experimental.Delegate;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Cheng Yufei
 * @create 2020-09-04 11:20
 **/
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
