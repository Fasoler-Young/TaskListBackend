package com.javabegin.tasklist.backendspringboot.service;

import com.javabegin.tasklist.backendspringboot.entity.TaskEntity;
import com.javabegin.tasklist.backendspringboot.repo.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository repository;
    


    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public TaskEntity update(TaskEntity task){
        return repository.save(task);
    }

    public TaskEntity add(TaskEntity task){
        return repository.save(task);
    }

    public void deleteById(Integer id){
        repository.deleteById(id);
    }

    public TaskEntity findById(Integer id){
        return repository.findById(id).get();
    }

    public List<TaskEntity> findAll(){
        return repository.findAll();
    }

    public Page<TaskEntity> findAllByParam(String title, Boolean completed, Integer priorityId, Integer categoryId, Pageable pageable){
        return repository.findAllByParam(title, completed, priorityId, categoryId, pageable);
    }
}
