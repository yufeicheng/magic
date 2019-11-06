package com.magic.interview.service.validated;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Cheng Yufei
 * @create 2019-11-06 15:07
 *
 * 自定义时间检验
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.TYPE,ElementType.PARAMETER})
@Constraint(validatedBy = MyCheckValidated.class)
@Repeatable(MyCheck.MyChecks.class)
public @interface MyCheck {

    String startTime();

    String endTime();

    String message() default "{javax.validation.constraints.Max.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD,ElementType.TYPE,ElementType.PARAMETER})
    @interface MyChecks{
        MyCheck[] value();
    }

}
