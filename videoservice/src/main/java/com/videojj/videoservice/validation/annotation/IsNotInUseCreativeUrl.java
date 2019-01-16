package com.videojj.videoservice.validation.annotation;

import com.videojj.videoservice.validation.validator.IsNotInUseCreativeUrlValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Constraint(validatedBy = IsNotInUseCreativeUrlValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IsNotInUseCreativeUrl {
    String message() default "{com.videojj.validation.IsNotInUseCreativeUrl.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
