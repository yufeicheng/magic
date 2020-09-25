package com.magic.interview.service.ordernum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Cheng Yufei
 * @create 2020-09-09 18:14
 **/
@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private OrderGenService orderGenService;

	@GetMapping(value = "/generate", produces = "application/json;charset=UTF-8")
	public String generate() {
		orderGenService.generate();
		return "";
	}

	@GetMapping(value = "/getResult", produces = "application/json;charset=UTF-8")
	public String getResult() {
		orderGenService.getResult();
		return "";
	}

}
