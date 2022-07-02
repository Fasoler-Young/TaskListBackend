package com.javabegin.tasklist.backendspringboot.service;


import com.javabegin.tasklist.backendspringboot.entity.CategoryEntity;
import com.javabegin.tasklist.backendspringboot.repo.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {this.repository=repository;}

    public CategoryEntity update(CategoryEntity category){
        return repository.save(category);
    }

    public CategoryEntity add(CategoryEntity category){
        return repository.save(category);
    }

    public void deleteByIdAndUserId(Integer id, Integer userId){
        repository.deleteByIdAndUserId(id, userId);
    }

    public CategoryEntity findById(Integer id){
        return repository.findById(id).get();
    }

    public List<CategoryEntity> findAllByUserIdOrderById(Integer userId){
        return repository.findAllByUserIdOrderById(userId);
    }

    public List<CategoryEntity> findByTitleAndUserId(String text, Integer id){
        return repository.findByTitleAndUserId(text, id);
    }

    public CategoryEntity findByIdAndUserId(Integer id, Integer userId){
        return repository.findByIdAndUserId(id, userId);
    }
}
