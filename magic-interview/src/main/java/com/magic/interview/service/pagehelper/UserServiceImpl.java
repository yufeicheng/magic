package com.magic.interview.service.pagehelper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.pagehelper.PageInfo;
import com.magic.dao.mapper.UserMapper;
import com.magic.dao.model.User;
import com.magic.dao.model.UserExample;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author Cheng Yufei
 * @create 2020-10-27 11:41
 **/
@Service
@Slf4j
public class UserServiceImpl implements BaseService<User, UserExample> {

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private ObjectMapper objectMapper;

	public ObjectNode getPageInfo(Integer pageFrom, Integer pageSize) {

		//pagehelper 分页每次请求时都会按条件count(0)一次
		UserExample userExample = new UserExample();
		userExample.setOrderByClause("id desc");
		userExample.createCriteria().andIdGreaterThanOrEqualTo(5);
		PageInfo<User> page = getPage(pageFrom, pageSize, userExample);

		/*UserExample userExample = new UserExample();
		userExample.setOffset((pageFrom-1)*pageSize);
		userExample.setLimit(pageSize);
		userExample.setOrderByClause("id desc");
		userExample.createCriteria().andIdGreaterThanOrEqualTo(5);
		List<User> users = userMapper.selectByExample(userExample);*/
		ObjectNode objectNode = objectMapper.createObjectNode();
		//objectNode.put("total", page.getTotal());
		objectNode.putPOJO("list", page.getList());
		objectNode.put("page", pageFrom);
		return objectNode;
	}

	@Override
	public List getList(UserExample userExample) {
		List<User> users = userMapper.selectByExample(userExample);
		return users;
	}
}
