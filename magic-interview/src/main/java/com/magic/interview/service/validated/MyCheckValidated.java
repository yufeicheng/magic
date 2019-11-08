package com.magic.interview.service.validated;

import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.util.Date;
import java.util.Objects;

/**
 * {@link MyCheck} 自定义校验规则
 * @author Cheng Yufei
 * @create 2019-11-06 15:13
 **/
public class MyCheckValidated implements ConstraintValidator<MyCheck,Object> {

    private String startTime;

    private String endTime;

    private FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");

    @Override
    public void initialize(MyCheck constraintAnnotation) {
        this.startTime = constraintAnnotation.startTime();
        this.endTime = constraintAnnotation.endTime();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        if (Objects.isNull(value)) {
            return true;
        }

        BeanWrapper beanWrapper = new BeanWrapperImpl(value);
        String start = ((String) beanWrapper.getPropertyValue(this.startTime));
        String end = ((String) beanWrapper.getPropertyValue(this.endTime));

        if (Objects.isNull(startTime) || Objects.isNull(endTime)) {
            return true;
        }

        try {
            if (format.parse(start).before(format.parse(end))) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }
}
