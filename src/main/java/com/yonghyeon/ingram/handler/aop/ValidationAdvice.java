package com.yonghyeon.ingram.handler.aop;

import com.yonghyeon.ingram.handler.CustomValidationApiException;
import com.yonghyeon.ingram.handler.CustomValidationException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

// 공통처리
@Component
@Aspect
public class ValidationAdvice {

    // 함수의 실행 전과 후에 모두 관여
    // 컨트롤러 안에 있는 모든 함수가 실행될 때 작동
    @Around("execution(* com.yonghyeon.ingram.web.api.*Controller.*(..))")
    // ProceedingJoinPoint가 Controller의 메서드의 매개변수와 내부에 접근할 수 있음
    public Object apiAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        // 메서드의 매개변수에 접근
        Object[] args = proceedingJoinPoint.getArgs();
        for(Object arg : args) {

            if(arg instanceof BindingResult) {

                BindingResult bindingResult = (BindingResult) arg;

                if(bindingResult.hasErrors()) {
                    Map<String, String> errorMap = new HashMap<>();

                    for(FieldError error : bindingResult.getFieldErrors()) {
                        errorMap.put(error.getField(), error.getDefaultMessage());
                    }
                    throw new CustomValidationApiException("유효성 검사 실패", errorMap);
                }
            }
        }

        // apiAdvice 함수가 먼저 실행된 후에 다시 Controller의 메서드로 돌아감
        return proceedingJoinPoint.proceed();
    }

    @Around("execution(* com.yonghyeon.ingram.web.controller.*Controller.*(..))")
    public Object advice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        Object[] args = proceedingJoinPoint.getArgs();
        for(Object arg : args) {

            if(arg instanceof BindingResult) {

                BindingResult bindingResult = (BindingResult) arg;

                if(bindingResult.hasErrors()) {
                    Map<String,String> errorMap = new HashMap<>();

                    for(FieldError error : bindingResult.getFieldErrors()) {
                        errorMap.put(error.getField(), error.getDefaultMessage());
                    }
                    throw new CustomValidationException("유효성 검사 실패", errorMap);
                }
            }
            System.out.println(arg);
        }

        return proceedingJoinPoint.proceed();
    }
}
