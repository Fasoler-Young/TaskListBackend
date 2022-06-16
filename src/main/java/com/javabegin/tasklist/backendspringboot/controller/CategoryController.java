package com.javabegin.tasklist.backendspringboot.controller;

import com.javabegin.tasklist.backendspringboot.entity.CategoryEntity;
import com.javabegin.tasklist.backendspringboot.repo.CategoryRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/test")
    @Operation(summary = "Test")
    public List<CategoryEntity> test(){
        return categoryRepository.findAll();
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "find one")
    public Optional<CategoryEntity> findOne(@PathVariable Integer id){
        return categoryRepository.findById(id);
    }

    @PostMapping("/add")
    @Operation(summary = "Add new category")
    public ResponseEntity<CategoryEntity> addCategory(@RequestBody CategoryEntity category){
        if(category.getId() != null && category.getId() != 0){
            return new ResponseEntity("redundant param: id must be null", HttpStatus.NOT_ACCEPTABLE);
        }

        if( category.getCompletedCount() != null && category.getCompletedCount() != 0 ){
            return new ResponseEntity("redundant param: completed count must be null", HttpStatus.NOT_ACCEPTABLE);
        }

        if( category.getUncompletedCount() != null && category.getUncompletedCount() != 0 ){
            return new ResponseEntity("redundant param: uncompleted count must be null", HttpStatus.NOT_ACCEPTABLE);
        }

        if(category.getTitle() == null || category.getTitle().trim().length() == 0){
            return new ResponseEntity("missed param: Title", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(categoryRepository.save(category));
    }

    @PutMapping("/update")
    @Operation(summary = "Update category")
    public ResponseEntity<CategoryEntity> update(@RequestBody CategoryEntity category){
         if( category.getId() == null || category.getId() == 0){
             return new ResponseEntity("missing param: id must not be bull", HttpStatus.NOT_ACCEPTABLE);
         }
         if(category.getTitle() == null || category.getTitle().trim().length() == 0){
             return new ResponseEntity("missing param: title must not be bull", HttpStatus.NOT_ACCEPTABLE);
         }

        if( category.getCompletedCount() == null ){
            return new ResponseEntity("redundant param: completed count must not be null", HttpStatus.NOT_ACCEPTABLE);
        }

        if( category.getUncompletedCount() == null){
            return new ResponseEntity("redundant param: uncompleted count must not be null", HttpStatus.NOT_ACCEPTABLE);
        }



        return ResponseEntity.ok(categoryRepository.save(category));

    }

}
