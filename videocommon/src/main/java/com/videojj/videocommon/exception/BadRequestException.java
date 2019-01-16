package com.videojj.videocommon.exception;

import org.springframework.http.HttpStatus;

/**
 *
 */
public class BadRequestException extends AbstractHttpException {


    public BadRequestException(String str) {
        super(str);
        setHttpStatus(HttpStatus.BAD_REQUEST);
    }
}
