package com.videojj.videoservice.validation.validator;

import com.videojj.videocommon.exception.ServiceException;
import com.videojj.videoservice.service.CheckService;
import com.videojj.videoservice.validation.annotation.IsNotInUseCreativeUrl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 素材未使用通过验证url 验证器
 *
 * @author zhangzhewen
 * @date 2018/11/1
 */
public class IsNotInUseCreativeUrlValidator implements ConstraintValidator<IsNotInUseCreativeUrl, String> {

    @Autowired
    private CheckService checkService;

    @Override
    public void initialize(IsNotInUseCreativeUrl constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            if (checkService.isInUseCreativeUrl(value)) {
                return false;
            }
        }catch (ServiceException e){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addConstraintViolation();
            return false;
        }
        return true;
    }
}
