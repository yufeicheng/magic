package com.magic.interview.service.time_transfer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

/**
 *
 * @author Cheng Yufei
 * @create 2020-10-16 10:19
 **/
@Getter
@Setter
@ToString
public class TimeDto {

	/**
	 * JsonSerialize: 序列化返回给前端时，自定义后返回时间戳
	 * JsonDeserialize: 接受前端时间戳参数，反序列化转化为LocalDateTime
	 */
	@JsonSerialize(using = JsonSerial.class)
	@JsonDeserialize(using = JsonDeserial.class)
	LocalDateTime timestamp;

	/**
	 * @RequestBody(TimeDto dto) 时，前端传参以pattern形式传此值,String会转为Date。
	 * 后端返前端时，也会以pattern形式返回
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
	Date fromRequestBodyTime;

	/**
	 * 1.@DateTimeFormat在使用@RequestBody无效；
	 *
	 * 2.Controller中参数（TimeDto dto），不带有@RequestParam，将请求格式为pattern形式的String转为Date
	 * 请求方式：/time/timestamp?fromRequestParamTime=2020-11-16 12:46:53&fromRequestParamInt=1122，会自动对应TimeDto的属性。
	 *
	 * 3.在后端返回前端时，此属性成为时间戳，可在application.yml中设spring.jackson.date-format: yyyy-MM-dd HH:mm:ss，如果有类 实现了 WebMvcConfigurer时，在 application中的配置是无效的,仍会返回时间戳。
	 * 可用@JsonFormat 设置返回前端格式
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
	Date fromRequestParamTime;

	Integer fromRequestParamInt;

}
