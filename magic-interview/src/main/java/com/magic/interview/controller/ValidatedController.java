package com.magic.interview.controller;

import com.alibaba.fastjson.JSON;
import com.magic.interview.service.validated.LombokDto;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @author Cheng Yufei
 * @create 2019-11-05 11:06
 **/
@RestController
@RequestMapping("/non")
@Validated
@Slf4j
public class ValidatedController {

    /**
     * 基本注解校验测试
     * @param name
     * @param id
     * @param size
     * @return
     */
    @GetMapping("/testNonNull")
    public String testNonNull(@NotBlank(message = "name 不能为空") String name, @Range(min = 2, max = 5, message = "id 不在指定范围内") Integer id,
                              @Size(min = 2, max = 5, message = "size大小不在给定范围") String[] size) {
        LombokDto lombokDto = new LombokDto();
        lombokDto.setName("bj");
        lombokDto.setTime(new Date());
        log.info(">>>>{}",lombokDto.toString());

        //return lombokDto;
        return JSON.toJSONString(lombokDto);
    }

    /**
     * LombokDto 添加自定义@MyCheck 校验注解测试
     * @param dto
     * @return
     */
    @PostMapping("/myCheck")
    public String myCheck(@RequestBody @Validated LombokDto dto) {

        return JSON.toJSONString(dto);
    }
}

/**
 * @AssertFalse 所注解的元素必须是Boolean类型，且值为false
 * @AssertTrue 所注解的元素必须是Boolean类型，且值为true
 * @DecimalMax 所注解的元素必须是数字，且值小于等于给定的值
 * @DecimalMin 所注解的元素必须是数字，且值大于等于给定的值
 * @Digits 所注解的元素必须是数字，且值必须是指定的位数
 * @Future 所注解的元素必须是将来某个日期
 * @Max 所注解的元素必须是数字，且值小于等于给定的值
 * @Min 所注解的元素必须是数字，且值小于等于给定的值
 * @Range 所注解的元素需在指定范围区间内
 * @NotNull 所注解的元素值不能为null
 * @NotBlank 所注解的元素值有内容
 * @Null 所注解的元素值为null
 * @Past 所注解的元素必须是某个过去的日期
 * @PastOrPresent 所注解的元素必须是过去某个或现在日期
 * @Pattern 所注解的元素必须满足给定的正则表达式
 * @Size 所注解的元素必须是String、集合或数组，且长度大小需保证在给定范围之内
 * @Email 所注解的元素需满足Email格式
 */
