package com.javabegin.tasklist.backendspringboot.controller;

import com.javabegin.tasklist.backendspringboot.service.UserDetailsServiceImpl;
import lombok.Getter;
import org.springframework.security.core.context.SecurityContextHolder;

@Getter
public abstract class SupportMethods {

    private final UserDetailsServiceImpl userDetailsService;

    public SupportMethods(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public Boolean isNullOrEmpty(String str){
        return str == null || str.trim().isEmpty();
    }

    public String getCurrentUserLogin(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public Integer getCurrentUserId(){
        return userDetailsService.findUserByLogin(getCurrentUserLogin()).getId();
    }
}
