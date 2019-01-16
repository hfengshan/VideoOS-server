package com.videojj.videoservice.annotation;

import com.videojj.videoservice.enums.OperationLogTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * added by
 * 这个注解 自动记录方法的操作日志
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OperationLogAnnotationService {
    int[] descArgPositions() default -1;
    String[] fieldNames() default "";
    OperationLogTypeEnum type();
}