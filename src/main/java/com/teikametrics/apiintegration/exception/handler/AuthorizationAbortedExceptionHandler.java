package com.teikametrics.apiintegration.exception.handler;

import com.teikametrics.apiintegration.exception.AuthorizationAbortedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice

public class AuthorizationAbortedExceptionHandler {

    @ExceptionHandler(AuthorizationAbortedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleAbortAuthorization(AuthorizationAbortedException authorizationAbortedException) {

        return new ResponseEntity<Object> (authorizationAbortedException.getReason(), authorizationAbortedException.getStatus());
    }


}
