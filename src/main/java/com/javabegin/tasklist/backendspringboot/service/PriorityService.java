package com.javabegin.tasklist.backendspringboot.service;


import com.javabegin.tasklist.backendspringboot.entity.PriorityEntity;
import com.javabegin.tasklist.backendspringboot.repo.PriorityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriorityService {
    private final PriorityRepository repository;


    public PriorityService(PriorityRepository repository) {
        this.repository = repository;
    }

    public PriorityEntity update(PriorityEntity priority){
        return repository.save(priority);
    }

    public PriorityEntity add(PriorityEntity priority){
        return repository.save(priority);
    }

    public void deleteById(Integer id){
        repository.deleteById(id);
    }

    public PriorityEntity findById(Integer id){
        return repository.findById(id).get();
    }

    public List<PriorityEntity> findAllByUserId(Integer userId){
        return repository.findAllByUserId(userId);
    }

    public List<PriorityEntity> findByTitle(String text, Integer userId){
        return repository.findByTitle(text, userId);
    }

    public void deleteByIdAndUserId(Integer id, Integer userId){
        repository.deleteByIdAndUserId(id, userId);
    }

    public PriorityEntity findByIdAndUserId(Integer id, Integer userId){
        return repository.findByIdAndUserId(id, userId);
    }
}
