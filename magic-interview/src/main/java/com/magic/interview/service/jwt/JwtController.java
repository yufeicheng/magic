package com.magic.interview.service.jwt;

import com.google.common.collect.Lists;
import com.magic.base.dto.Result;
import com.magic.interview.service.validated.LombokDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Cheng Yufei
 * @create 2020-09-09 18:14
 **/
@RestController
@RequestMapping("/jwt")
public class JwtController {

	@Autowired
	private JwtService jwtService;

	@GetMapping(value = "/login/{version}", headers = {"api-version=2","!code"} , produces = "application/json;charset=UTF-8")
	public String login(@RequestParam String uid, @PathVariable Integer version) {
		return jwtService.generatorJwtToken2(uid, version);
	}

	@GetMapping(value = "/getInfo", produces = "application/json;charset=UTF-8")
	public String getInfo() {
		LombokDto dto = new LombokDto();
		dto.setCid(10000);
		dto.setName("JWT");
		return dto.toString();
	}

}
