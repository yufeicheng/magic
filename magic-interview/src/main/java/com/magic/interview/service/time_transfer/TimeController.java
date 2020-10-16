package com.magic.interview.service.time_transfer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 *
 * @author Cheng Yufei
 * @create 2020-10-16 10:26
 **/
@RestController
@RequestMapping("/time")
public class TimeController {

	@Autowired
	private ObjectMapper objectMapper;

	@GetMapping("/timestamp")
	public TimeDto timestamp(@RequestBody TimeDto dto, TimeDto dtoParam) throws JsonProcessingException {
		LocalDateTime now = LocalDateTime.now();
		TimeDto timeDto = new TimeDto();
		//timeDto.setTimestamp(now);
		timeDto.setFromRequestBodyTime(dto.getFromRequestBodyTime());
		timeDto.setFromRequestParamTime(dtoParam.getFromRequestParamTime());
		//return objectMapper.writeValueAsString(timeDto);
		System.out.println("dtoBody:" + dto.toString());
		System.out.println("dtoParam:" + dtoParam.toString());
		return timeDto;
	}
}
