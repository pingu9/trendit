package com.trendit.common.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class BindingResultAdvice {
    @Pointcut("execution(com.trendit.api.controller.BoardController..*.*(..))")
    public void cut() {

    }
}
