package com.code.configs;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author GYJ
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(RestResponseAdvice.class);

    @ExceptionHandler(Exception.class)
    public Exception exceptionHandler(Exception e) {
        log.error("Exception ",e);
        e.printStackTrace();
        return e;
    }
}