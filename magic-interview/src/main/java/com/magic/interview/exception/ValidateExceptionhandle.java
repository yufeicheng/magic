package com.magic.interview.exception;

import com.alibaba.fastjson.JSON;
import com.magic.base.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.io.IOException;
import java.util.Set;

/**
 * 非RequestBody形式的参数校验异常处理
 * @author Cheng Yufei
 * @create 2019-11-08 15:33
 **/
@RestControllerAdvice
@Slf4j
public class ValidateExceptionhandle {

    @ExceptionHandler(value = ValidationException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public void handle(ValidationException ex, HttpServletResponse response) {

        if (ex instanceof ConstraintViolationException) {
            Set<ConstraintViolation<?>> violations = ((ConstraintViolationException) ex).getConstraintViolations();
            violations.stream().forEach(v->log.error(v.getMessage()));
        }
        try {
            response.getWriter().write(JSON.toJSONString(Result.fail("bad request")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
