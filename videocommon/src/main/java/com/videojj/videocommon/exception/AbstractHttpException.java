package com.videojj.videocommon.exception;

import org.springframework.http.HttpStatus;

public abstract class AbstractHttpException extends RuntimeException{

    private static final long serialVersionUID = -1L;

    protected HttpStatus httpStatus;

    public AbstractHttpException(String msg){
        super(msg);
    }

    public AbstractHttpException(String msg, Exception e){
        super(msg,e);
    }

    protected void setHttpStatus(HttpStatus httpStatus){
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
