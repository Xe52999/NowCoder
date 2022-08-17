package com.xe.mynowcoder.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
//
//@Component
//@Aspect
public class AlphaAspect {

    //返回值是* service包下的所有类中的所有方法，'..'代表所有参数
    @Pointcut("execution(* com.xe.mynowcoder.service.*.*(..))")
    public void pointcut() {

    }

    @Before("pointcut()")
    public void before() {
        System.out.println("before");
    }

    @After("pointcut()")
    public void after() {
        System.out.println("after");
    }

    @AfterReturning("pointcut()")
    public void afterRetuning() {
        System.out.println("afterRetuning");
    }

    @AfterThrowing("pointcut()")
    public void afterThrowing() {
        System.out.println("afterThrowing");
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("around before");
        //目标对象执行
        Object obj = joinPoint.proceed();
        System.out.println("around after");
        return obj;
    }

}
