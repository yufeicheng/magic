package com.magic.interview.exception;

import com.google.gson.Gson;
import com.magic.base.dto.Result;
import com.magic.base.dto.enums.RespStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.NotReadablePropertyException;
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
	 *
	 * @param ex
	 * @param response
	 */
	@ExceptionHandler(value = ConstraintViolationException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Result handle(ConstraintViolationException ex, HttpServletResponse response) {

		log.error(ex.getMessage());
		return Result.fail(RespStatusEnum.PARAMETER_ERROR.getStatus(), ex.getMessage());
		/*response.setContentType("application/json; charset=UTF-8");
		try (PrintWriter writer = response.getWriter()) {
			writer.write(gson.toJson(Result.failParameter(ex.getMessage())));
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}

	/**
	 * @param ex
	 * @param response
	 * @RequestBody形式校验异常
	 */
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	@ResponseStatus(code = HttpStatus.OK)
	@ResponseBody
	public Result handle(MethodArgumentNotValidException ex, HttpServletResponse response) {
		BindingResult bindingResult = ex.getBindingResult();
		StringBuilder stringBuilder = new StringBuilder();
		//@MyCheck自定义校验规则时异常信息在getAllErrors()方法
		bindingResult.getAllErrors().stream().forEach(b->stringBuilder.append(b.getObjectName()).append(":").append(b.getDefaultMessage()).append(";"));
		bindingResult.getFieldErrors().stream().forEach(b -> stringBuilder.append(b.getField()).append(":").append(b.getDefaultMessage()).append(";"));
		String error = stringBuilder.toString();
		log.error(error);
		return Result.fail(RespStatusEnum.PARAMETER_ERROR.getStatus(), error);
	/*
	使用 @ResponseBody 替代

	try (PrintWriter writer = response.getWriter()) {
			writer.write(gson.toJson(Result.failParameter(error)));
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}

	/**
	 * 自定义集合类型ValidationList形式校验异常
	 * @param ex
	 * @param response
	 * @return
	 */
	@ExceptionHandler(value = NotReadablePropertyException.class)
	@ResponseStatus(code = HttpStatus.OK)
	@ResponseBody
	public Result handle(NotReadablePropertyException ex, HttpServletResponse response) {

		log.error(ex.getMessage());
		return Result.fail(RespStatusEnum.PARAMETER_ERROR.getStatus(), ex.getMessage());
	}
}
