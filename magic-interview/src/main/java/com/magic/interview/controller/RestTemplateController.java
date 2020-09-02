package com.magic.interview.controller;

import com.magic.interview.service.http_client.HttpClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Cheng Yufei
 * @create 2019-11-05 11:06
 **/
@RestController
@RequestMapping("/httpClient")
@Validated
@Slf4j
public class RestTemplateController {

	@Autowired
	private HttpClientService httpClientService;


	@GetMapping("/restTemplate")
	public String testRestTemplate() throws InterruptedException {
        httpClientService.testRestTemplate();
		return "success";
	}
}

