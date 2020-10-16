package com.magic.interview.service.time_transfer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 *
 * @author Cheng Yufei
 * @create 2020-10-16 15:32
 **/
public class JsonDeserial extends JsonDeserializer<LocalDateTime> {
	@Override
	public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.valueOf(p.getText())), ZoneId.of("Asia/Shanghai"));
	}
}
