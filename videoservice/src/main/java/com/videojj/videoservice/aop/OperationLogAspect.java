package com.videojj.videoservice.aop;

import com.videojj.videoservice.annotation.OperationLogAnnotationService;
import com.videojj.videoservice.enums.OperationLogTypeEnum;
import com.videojj.videoservice.service.OperationLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.text.MessageFormat;

/**
 * Created by zhangzhewen on 18/10/11.
 */
@Component
@Aspect
public class OperationLogAspect {

    private static Logger log = LoggerFactory.getLogger(OperationLogAspect.class);

    @Resource
    private OperationLogService operationLogService;

    @AfterReturning("@annotation(operationLogAnnotationService)")
    public void afterReturning(JoinPoint joinPoint, OperationLogAnnotationService operationLogAnnotationService) {
        try {
            OperationLogTypeEnum operationLogTypeEnum = operationLogAnnotationService.type();
            int[] annotationDescArgPositions = operationLogAnnotationService.descArgPositions();
            Object[] targetDescArgPositions = new Object[annotationDescArgPositions.length];
            String[] fieldNames = operationLogAnnotationService.fieldNames();
            boolean attrNamesResolverFlag = (fieldNames.length==1&&fieldNames[0].isEmpty())?false:true;
            if (operationLogAnnotationService.descArgPositions()[0] != -1) {
                for (int i = 0; i < annotationDescArgPositions.length; i++) {
                    if(attrNamesResolverFlag&&!fieldNames[i].isEmpty()){
                        Object targetFieldValue=null;
                        for(String fieldName : fieldNames[i].split("\\.")){
                            if(targetFieldValue==null){
                                targetFieldValue = joinPoint.getArgs()[annotationDescArgPositions[i]];
                            }
                            Field field = targetFieldValue.getClass().getDeclaredField(fieldName);
                            field.setAccessible(true);
                            targetFieldValue = field.get(targetFieldValue);
                        }
                        targetDescArgPositions[i] = targetFieldValue;
                    }else{
                        targetDescArgPositions[i] = joinPoint.getArgs()[annotationDescArgPositions[i]];
                    }
                }
            }
            String operationDesc = MessageFormat.format(operationLogTypeEnum.getOperationDescTemplate(), targetDescArgPositions);
            operationLogService.addOperationLog(operationDesc, operationLogTypeEnum.getOperationType());
        } catch (Exception e) {
            log.error("操作日志执行异常！", e);
        }
    }

}
