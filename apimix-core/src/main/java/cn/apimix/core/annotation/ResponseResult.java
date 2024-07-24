package cn.apimix.core.annotation;

import java.lang.annotation.*;

/**
 * 返回结果注解
 * @author Hor
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Documented
public @interface ResponseResult { }