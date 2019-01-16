package com.videojj.videoservice.validation.validator;

import com.videojj.videoservice.service.CheckService;
import com.videojj.videoservice.validation.annotation.IsNotInUseCreativeId;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 素材未使用通过验证id 验证器
 *
 * @author zhangzhewen
 * @date 2018/11/1
 */
public class IsNotInUseCreativeIdValidator implements ConstraintValidator<IsNotInUseCreativeId, Integer> {

    @Autowired
    private CheckService checkService;

    @Override
    public void initialize(IsNotInUseCreativeId constraintAnnotation) {
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (checkService.isInUseCreativeId(value)) {
            return false;
        }
        return true;
    }
}
