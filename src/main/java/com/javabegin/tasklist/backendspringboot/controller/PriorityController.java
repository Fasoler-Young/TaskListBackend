package com.javabegin.tasklist.backendspringboot.controller;

import com.javabegin.tasklist.backendspringboot.entity.CategoryEntity;
import com.javabegin.tasklist.backendspringboot.entity.PriorityEntity;
import com.javabegin.tasklist.backendspringboot.repo.PriorityRepository;
import com.javabegin.tasklist.backendspringboot.search.PrioritySearchValues;
import com.javabegin.tasklist.backendspringboot.service.PriorityService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/priority")
public class PriorityController {

    private final PriorityService service;

    public PriorityController(PriorityService service) {
        this.service = service;
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

        return ResponseEntity.ok(service.add(priority));
    }

    @GetMapping("/id/{id}")
    @Operation(summary = "Get Priority by id")
    public ResponseEntity<PriorityEntity> getById(@PathVariable Integer id){
        PriorityEntity priority = null;
        try {
            priority = service.findById(id);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity("id = " + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(priority);
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

        return ResponseEntity.ok(service.update(priority));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete priority by id")
    public ResponseEntity delete(@PathVariable Integer id){
        try{
            service.deleteById(id);

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity("id = " + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all priorities")
    public List<PriorityEntity> findAll(){
        return service.findAllByOrderByTitleAsc();
    }


    // Поиск по любым параметрам PrioritySearchValues
    @PostMapping("/search")
    public ResponseEntity<List<PriorityEntity>> search(@RequestBody PrioritySearchValues prioritySearchValues){
        return ResponseEntity.ok(service.findByTitle(prioritySearchValues.getText()));
    }
}
