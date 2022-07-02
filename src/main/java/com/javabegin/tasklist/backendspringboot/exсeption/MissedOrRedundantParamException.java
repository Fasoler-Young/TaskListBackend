package com.javabegin.tasklist.backendspringboot.exсeption;

public class MissedOrRedundantParamException extends Exception{

    public MissedOrRedundantParamException(String message) {
        super(message);
    }
}
