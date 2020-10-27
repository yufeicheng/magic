package com.magic.interview.service.pagehelper;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 *
 * @author Cheng Yufei
 * @create 2020-10-27 14:35
 **/
public interface BaseService<T, K> {

	default PageInfo<T> getPage(Integer pageNum, Integer pageSize, K k) {
		PageInfo<T> ePageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> getList(k));
		return ePageInfo;
	}

	List<T> getList(K k);
}
