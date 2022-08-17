package com.xe.mynowcoder.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
//修饰方法，用于标识该方法是否需要登录才能访问
public @interface LoginRequired {
}
