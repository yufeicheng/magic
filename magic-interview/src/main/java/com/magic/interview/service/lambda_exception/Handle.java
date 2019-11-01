package com.magic.interview.service.lambda_exception;

import java.util.function.Function;

/**
 * @author Cheng Yufei
 * @create 2019-10-31 16:32
 **/
public interface Handle {

   static <T,R> Function<T,R> handle(CheckFunction<T,R> checkFunction){

       return t -> {
           try {
               return checkFunction.apply(t);
           } catch (Exception ex) {
               throw new RuntimeException(ex);
           }
       };
   }
}
