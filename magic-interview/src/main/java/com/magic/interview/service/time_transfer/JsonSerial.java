package com.magic.interview.service.time_transfer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 序列化时将时间转为时间戳返回前端
 * @author Cheng Yufei
 * @create 2020-10-16 10:20
 **/
public class JsonSerial extends JsonSerializer<LocalDateTime> {
	@Override
	public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeNumber(value.atZone(ZoneId.of("Asia/Shanghai")).toInstant().toEpochMilli());
	}
}
