package com.javabegin.tasklist.backendspringboot.controller;

import com.javabegin.tasklist.backendspringboot.entity.StatEntity;
import com.javabegin.tasklist.backendspringboot.service.StatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatController {

    private final StatService service;

    public StatController(StatService service) {
        this.service = service;
    }

    @GetMapping("/stat")
    public ResponseEntity<StatEntity> findById(){
        Integer defaultId = 1;
        return ResponseEntity.ok(service.findById(defaultId));
    }
}
