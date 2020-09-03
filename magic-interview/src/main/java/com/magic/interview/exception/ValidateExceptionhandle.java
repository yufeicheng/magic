package com.magic.interview.exception;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.magic.base.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * validate参数校验异常处理
 *
 * @author Cheng Yufei
 * @create 2019-11-08 15:33
 **/
@RestControllerAdvice
@Slf4j
public class ValidateExceptionhandle {

	@Autowired
	private Gson gson;

	/**
	 * requestParam/PathVariable形式校验异常
	 * @param ex
	 * @param response
	 */
	@ExceptionHandler(value = ConstraintViolationException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Result handle(ConstraintViolationException ex, HttpServletResponse response) {

		log.error(ex.getMessage());
		return Result.failParameter(ex.getMessage());
		/*response.setContentType("application/json; charset=UTF-8");
		try (PrintWriter writer = response.getWriter()) {
			writer.write(gson.toJson(Result.failParameter(ex.getMessage())));
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}

	/**
	 * @RequestBody形式校验异常
	 * @param ex
	 * @param response
	 */
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	@ResponseStatus(code = HttpStatus.OK)
	@ResponseBody
	public Result handle(MethodArgumentNotValidException ex,HttpServletResponse response) {
		BindingResult bindingResult = ex.getBindingResult();
		StringBuilder stringBuilder = new StringBuilder();
		bindingResult.getFieldErrors().stream().forEach(b -> stringBuilder.append(b.getField()).append(":").append(b.getDefaultMessage()).append(";"));
		String error = stringBuilder.toString();
		log.error(error);
		return Result.failParameter(error);
	/*
	使用 @ResponseBody 替代

	try (PrintWriter writer = response.getWriter()) {
			writer.write(gson.toJson(Result.failParameter(error)));
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}

}
