package com.videojj.videoservice.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * added by
 * 这个注解 为了标记Controller层的分页方法
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PageControllerService {
    //页码参数名称
    String pageNumName() default "currentPage";
    //页面大小参数名称
    String pageSizeName() default "pageSize";
    //排序参数名称
    String orderByName() default "orderBy";
    //页码参数缺省值
    int pageNumDefaultValue() default 1;
    //页面大小缺省值
    int pageSizeDefaultValue() default 10;
    //排序缺省值
    String orderByDefaultValue() default "";
    //最大页面大小
    int MAX_PAGE_SIZE = Integer.MAX_VALUE-1;
}