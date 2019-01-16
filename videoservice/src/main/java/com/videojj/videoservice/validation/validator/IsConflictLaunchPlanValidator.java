package com.videojj.videoservice.validation.validator;

import com.videojj.videocommon.exception.ServiceException;
import com.videojj.videoservice.dto.AddLaunchPlanRequestDTO;
import com.videojj.videoservice.service.CheckService;
import com.videojj.videoservice.validation.annotation.IsConflictLaunchPlan;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 素材未使用通过验证url 验证器
 *
 * @author zhangzhewen
 * @date 2018/11/1
 */
public class IsConflictLaunchPlanValidator implements ConstraintValidator<IsConflictLaunchPlan, AddLaunchPlanRequestDTO> {

    @Autowired
    private CheckService checkService;


    @Override
    public void initialize(IsConflictLaunchPlan constraintAnnotation) {
    }

    @Override
    public boolean isValid(AddLaunchPlanRequestDTO value, ConstraintValidatorContext context) {
        try {
            /**如果与多个冲突，是逗号分割的字符串*/
            String launchPlanName = checkService.checkLaunchPlan(value);
            if (StringUtils.isNotEmpty(launchPlanName)) {
                if (context instanceof HibernateConstraintValidatorContext) {
                    context.unwrap(HibernateConstraintValidatorContext.class).addMessageParameter("launchPlanName", launchPlanName);
                }
                return false;
            }
        } catch (ServiceException e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addConstraintViolation();
            return false;
        }
        return true;
    }
}
