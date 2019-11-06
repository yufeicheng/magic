package com.magic.interview.service.validated;

/**
 * @author Cheng Yufei
 * @create 2019-11-05 10:36
 **/

public class ValidatedService {

    /**
     * dto中cid 有@NonNull ，set属性null值会校验报错
     */
    public static void testWith() {
        LombokDto lombokDto = new LombokDto();
        lombokDto.setName("北京");
        lombokDto.setCid(null);
        System.out.println(lombokDto);
    }

    public static void main(String[] args) {
        testWith();
    }
}
