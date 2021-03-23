package com.example.demo.aop;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Log4j2
@Component
public class PageViewAdvice {
    @Pointcut("execution(* com.example.demo.controller.web*.*(..))")
    public void addPageView() {
        log.info("aop!");
    }

    @Around("addPageView()")
    public void add(ProceedingJoinPoint point) {
        log.info("around");
    }
}
