package com.magic.interview.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;
import com.google.gson.GsonBuilder;
import com.magic.base.dto.Result;
import com.magic.interview.service.use_custom_starter.UseStarterService;
import com.magic.interview.service.validated.LombokDto;
import com.magic.interview.service.validated.MyCheck;
import com.magic.interview.service.validated.MyCheckValidated;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Cheng Yufei
 * @create 2019-11-05 11:06
 **/
@RestController
@RequestMapping("/non")
@Validated
@Slf4j
public class ValidatedController {

    @Autowired
    private UseStarterService useStarterService;

    /**
     * 基本注解校验测试
     *
     * @param name
     * @param id
     * @param size
     * @return
     */
    @GetMapping("/testNonNull/{uid}")
    public String testNonNull(@NotEmpty(message = "name 不能为空")  String name, @Range(min = 2, max = 5, message = "id 不在指定范围内")  Integer id,
                              @Size(min = 2, max = 5, message = "size大小不在给定范围") @RequestParam List<String> size, @Digits(integer = 3,fraction = 0) @PathVariable Double uid) {
        LombokDto lombokDto = new LombokDto();
        lombokDto.setName(name);
        lombokDto.setTime(new Date());
        log.info(">>>>{}", lombokDto.toString());
        //return lombokDto;
        return JSON.toJSONString(lombokDto);
    }

    /**
     * LombokDto 添加自定义@MyCheck 校验注解测试
     * 需处理BindingResult，否则
     *
     * @param dto
     * @return
     */
    @PostMapping("/myCheck")
    public Result myCheck( @Validated(LombokDto.GroupA.class) @RequestBody LombokDto dto) {
        //dto.setCid(null);
        log.info(dto.toString());
        return Result.success(dto);
    }

    @PostMapping("/list")
    public Result validateListDto(@RequestBody List<LombokDto> dtos) {
        return Result.success(dtos);
    }


    /**
     * 自定义starter使用
     * @return
     */
    @GetMapping("/customeStarter")
    public String useCostomStarter() {
        return useStarterService.use();
    }
}

    /**
     * @RequestParam与@PathVariable参数校验：
     *
     * @AssertFalse 所注解的元素必须是Boolean类型，且值为false
     * @AssertTrue 所注解的元素必须是Boolean类型，且值为true
     * @DecimalMax 所注解的元素必须是数字，且值小于等于给定的值
     * @DecimalMin 所注解的元素必须是数字，且值大于等于给定的值
     * @Digits  BigDecimal BigInteger CharSequence byte、short、int、long、double以及它们各自的包装器类型值必须是指定的位数
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
