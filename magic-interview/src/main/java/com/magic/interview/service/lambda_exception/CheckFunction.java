package com.magic.interview.service.lambda_exception;

/**
 * @author Cheng Yufei
 * @create 2019-10-31 16:26
 **/
@FunctionalInterface
public interface CheckFunction<T,R> {

    //抽象方法
    R apply(T t) throws Exception;

    static void staticMethod(){

    }

    default void defaultMethod() {

    }

    @Override
    boolean equals(Object obj);
}

/**
 * @FunctionalInterface: 函数式接口
 *
 * 1. 有且只有一个抽象方法；
 *
 * 2. 可以包含静态方法、default方法；
 *
 * 3. 声明覆盖Object中的方法不算抽象方法；
 *
 * 4. 可以使用一个lambda表达式 表示该接口的实现；
 *
 */
