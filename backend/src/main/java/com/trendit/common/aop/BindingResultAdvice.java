package com.trendit.common.aop;

import com.trendit.common.exception.BindingException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Aspect
@Component
public class BindingResultAdvice {
    @Pointcut("execution(* com.trendit.api.controller.BoardController.postBoard(..))" +
            "|| execution(* com.trendit.api.controller.KeywordController.addKeyword(..))" +
            "|| execution(* com.trendit.api.controller.KeywordController.addKeywordCompany(..))")
    public void pointCut() {

    }

    @Around("pointCut()")
    public Object ValidateHandler(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        BindingResult bindingResult = (BindingResult) args[1];
        if (bindingResult.hasErrors()) {
             throw new BindingException();
        }

        return joinPoint.proceed();
    }
}
