package com.magic.interview.Config;

import com.google.gson.Gson;
import com.magic.base.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private Gson gson;

	@Override
	public boolean supports(MethodParameter returnType, Class converterType) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

		if (body instanceof Result) {
			return body;
		}

		if (body instanceof String) {
			return gson.toJson(Result.success(body));
		}
		return Result.success(body);
	}
}

/**
 *  Controller 返回String类型时，
 *  【与 MvcConfig 重写configMessageConverters将 MappingJackson2HttpMessageConverter 设置为第一位无关，此方法无效。之前生效的原因是controller方法返回要么不是String类型，是String类型时，因为请求时候Accept设置，所以看起来此方法是有效的，其实是无效处理方法。 】
 *
 * 通过浏览器访问时，统一类型处理 RequestResponseBodyAdviceChain 类 processBody 方法 接受到的MediaType为text/html，表明浏览器接受的是 text/html的返回类型，同时
 *  HttpMessageConverter为  org.springframework.http.converter.StringHttpMessageConverter 转换类，此时就会报错：Result 转String类型错误。
 *
 *  一解决：在Controller不设置 produces = "application/json;charset=UTF-8"
 *  	1.统一返回类型处理针对String类型单独处理，返回String类型。
 *   	2. 需要请求方设置 Accept: application/json ,这样接受到的数据是json类型而不是String。
 *
 *   二解决：Controller设置 produces = "application/json;charset=UTF-8"
 *   	1. 统一返回类型对String特殊处理；
 *      2. 无需请求方设置Accept参数；
 *
 *==========================================================================================================================================
 *
 * @RequestMapping 中参数：
 *
 * 	consumes： 指定处理请求的提交内容类型（Content-Type），例如application/json, text/html;
 *  produces: 指定返回的内容类型，仅当request请求头中的(Accept)类型中包含该指定类型才返回；
 *  headers： 指定request中必须包含某些指定的header值，才能让该方法处理请求。 headers = {"api-version=2","!code"} ,只有请求的header中不含code，且含有api-version，值等于2的时候才会接受请求
 *
 * ==========================================================================================================================================
 *
 * Accept 头匹配规则：
 *  Accept：text/html,application/xml,application/json
 * 将按照如下顺序进行produces的匹配 ①text/html ②application/xml ③application/json
 * Accept：application/xml;q=0.5,application/json;q=0.9,text/html
 * 将按照如下顺序进行produces的匹配 ①text/html ②application/json ③application/xml
 *
 */
