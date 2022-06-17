package com.javabegin.tasklist.backendspringboot.service;

import com.javabegin.tasklist.backendspringboot.entity.StatEntity;
import com.javabegin.tasklist.backendspringboot.repo.StatRepository;
import org.springframework.stereotype.Service;

@Service
public class StatService {

    private final StatRepository repository;

    public StatService(StatRepository repository) {
        this.repository = repository;
    }

    public StatEntity findById(Integer id){
        return repository.findById(id).get();
    }
}
