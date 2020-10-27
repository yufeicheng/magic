package com.magic.interview.service.pagehelper;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Cheng Yufei
 * @create 2020-09-09 18:14
 **/
@RestController
@RequestMapping("/page")
public class PageController {

	@Autowired
	private UserServiceImpl userService;


	@GetMapping(value = "/getPageInfo", produces = "application/json;charset=UTF-8")
	public ObjectNode getPageInfo(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
		return userService.getPageInfo(pageNum, pageSize);
	}
}
