package com.javabegin.tasklist.backendspringboot.controller;

import com.javabegin.tasklist.backendspringboot.entity.PriorityEntity;
import com.javabegin.tasklist.backendspringboot.repo.PriorityRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/priority")
public class PriorityController {

    private final PriorityRepository priorityRepository;

    public PriorityController(PriorityRepository priorityRepository) {
        this.priorityRepository = priorityRepository;
    }


    @GetMapping("/test")
    public List<PriorityEntity> test(){
        return priorityRepository.findAll();
    }

    @PostMapping("/add")
    @Operation(summary = "Add new Priority")
    public ResponseEntity<PriorityEntity> addPriority(@RequestBody PriorityEntity priority){

        if(priority.getId() != null && priority.getId() != 0){
            return new ResponseEntity("redundant param: id Must be null", HttpStatus.NOT_ACCEPTABLE);
        }

        if(priority.getTitle() == null || priority.getTitle().trim().length() == 0){
            return new ResponseEntity("missed param: Title", HttpStatus.NOT_ACCEPTABLE);
        }

        if(priority.getColor() == null || priority.getColor().trim().length() == 0){
            return new ResponseEntity("missed param: color", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(priorityRepository.save(priority));
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "Get Priority by id")
    public Optional<PriorityEntity> getById(@PathVariable Integer id){
        return priorityRepository.findById(id);
    }

    @PutMapping("/update")
    @Operation(summary = "Update Priority")
    public ResponseEntity<PriorityEntity> update(@RequestBody PriorityEntity priority){
        if(priority.getId() == null || priority.getId() == 0){
            return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
        }

        if(priority.getTitle() == null || priority.getTitle().trim().length() == 0){
            return new ResponseEntity("missed param: Title", HttpStatus.NOT_ACCEPTABLE);
        }

        if(priority.getColor() == null || priority.getColor().trim().length() == 0){
            return new ResponseEntity("missed param: color", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(priorityRepository.save(priority));
    }
}
