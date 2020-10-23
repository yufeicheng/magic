package com.magic.interview.service.listener_expire_rediskey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author Cheng Yufei
 * @create 2020-10-23 11:22
 **/
@RestController
@RequestMapping("/listener")
public class ListenerController {

	@Autowired
	private RedisTemplate redisTemplate;

	@GetMapping("/setExpireKey")
	public String setExpireKey(@RequestParam String key, @RequestParam Integer expire) {

		redisTemplate.opsForValue().set(key, "过期key监听", expire, TimeUnit.SECONDS);
		return "success";
	}

}
