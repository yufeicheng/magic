package com.magic.interview.Config;

import com.magic.base.dto.Result;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author Cheng Yufei
 * @create 2020-09-03 16:39
 **/
@RestControllerAdvice
public class ReturnConfig implements ResponseBodyAdvice {

	@Override
	public boolean supports(MethodParameter returnType, Class converterType) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

		if (body instanceof Result) {
			return body;
		}
		return Result.success(body);
	}
}
