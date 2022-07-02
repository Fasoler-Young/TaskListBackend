package com.javabegin.tasklist.backendspringboot.exсeption;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerAdviceIMPL {

    @ExceptionHandler(MissedOrRedundantParamException.class)
    public ResponseEntity<Response> handleException(MissedOrRedundantParamException e){
        Response response = new Response(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
    }


}
