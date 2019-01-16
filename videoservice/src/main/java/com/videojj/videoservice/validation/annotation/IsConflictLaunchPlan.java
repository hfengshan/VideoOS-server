package com.videojj.videoservice.validation.annotation;

import com.videojj.videoservice.validation.validator.IsConflictLaunchPlanValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Constraint(validatedBy = IsConflictLaunchPlanValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IsConflictLaunchPlan {
    String message() default "{com.videojj.validation.IsConflictLaunchPlan.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
