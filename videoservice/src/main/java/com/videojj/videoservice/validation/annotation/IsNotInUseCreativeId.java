package com.videojj.videoservice.validation.annotation;

import com.videojj.videoservice.validation.validator.IsNotInUseCreativeIdValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Constraint(validatedBy = IsNotInUseCreativeIdValidator.class)
@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IsNotInUseCreativeId {
    String message() default "{com.videojj.validation.IsNotInUseCreativeId.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
