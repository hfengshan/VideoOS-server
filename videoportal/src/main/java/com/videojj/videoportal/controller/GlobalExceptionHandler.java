package com.videojj.videoportal.controller;

import com.videojj.videocommon.constant.Constants;
import com.videojj.videocommon.dto.base.BaseResponseDTO;
import com.videojj.videocommon.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Collection;
import java.util.function.Function;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(value = Exception.class)
    public BaseResponseDTO handleAllException(HttpServletRequest request, Exception e) {

        logger.error(e.getMessage(), e);

        return BaseResponseDTO.builder().resMsg("系统错误，请联系技术人员！").resCode(Constants.FAILCODE).build();

    }

    @ExceptionHandler(value = ServiceException.class)
    public BaseResponseDTO handleServiceException(HttpServletRequest request, ServiceException e) {

        logger.error(e.getMessage(), e);

        return BaseResponseDTO.builder().resMsg("业务错误：" + e.getMessage() + "，如解决不了，可联系技术人员").resCode(Constants.FAILCODE).build();

    }

    //方法基本参数的验证异常
    @ExceptionHandler(value = ConstraintViolationException.class)
    public BaseResponseDTO handleConstraintViolationException(ConstraintViolationException cve) {
        return getMessage(cve.getConstraintViolations(),o -> ((ConstraintViolation)o).getMessage());
    }

    //方法respose body对象参数的验证异常
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public BaseResponseDTO handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return getMessage(e.getBindingResult().getAllErrors(), o -> ((ObjectError)o).getDefaultMessage());
    }

    @ExceptionHandler(value = MissingServletRequestPartException.class)
    public BaseResponseDTO handleMissingServletRequestPartException(MissingServletRequestPartException e) {
        return BaseResponseDTO.builder().resMsg("请上传文件" + e.getRequestPartName()).resCode(Constants.FAILCODE).build();
    }

    //方法model对象参数的验证异常
    @ExceptionHandler(value = BindException.class)
    public BaseResponseDTO handleBindException(BindException e) {
        return getMessage(e.getAllErrors(), o -> ((ObjectError)o).getDefaultMessage());
    }

    private static <T extends Collection,U>  BaseResponseDTO getMessage(T t, Function<U,String> function){
        if (t.size() == 1) {
            return BaseResponseDTO.builder().resMsg(function.apply((U)t.iterator().next()))
                    .resCode(Constants.FAILCODE).build();
        } else {
            return BaseResponseDTO.builder().resMsg("发生以下错误: " +
                    t.stream()
                            .map(m -> "[" + function.apply((U)m) + "]")
                            .reduce((x, y) -> x + "," + y)
                            .orElse(StringUtils.EMPTY)
            ).resCode(Constants.FAILCODE).build();
        }
    }
}