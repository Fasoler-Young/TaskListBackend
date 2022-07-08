package com.javabegin.tasklist.backendspringboot.service;

import com.javabegin.tasklist.backendspringboot.entity.StatEntity;
import com.javabegin.tasklist.backendspringboot.repo.StatRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatService {

    private final StatRepository repository;

    public StatService(StatRepository repository) {
        this.repository = repository;
    }

    public StatEntity findByUserId(Integer userId){
        return repository.findByUserId(userId);
    }

    public List<StatEntity> findAll(){
        return repository.findAll();
    }
}
