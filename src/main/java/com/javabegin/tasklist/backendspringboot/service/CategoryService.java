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

    public void deleteById(Integer id){
        repository.deleteById(id);
    }

    public CategoryEntity findById(Integer id){
        return repository.findById(id).get();
    }

    public List<CategoryEntity> findAllByOrderById(){
        return repository.findAllByOrderById();
    }

    public List<CategoryEntity> findByTitle(String text){
        return repository.findByTitle(text);
    }
}
