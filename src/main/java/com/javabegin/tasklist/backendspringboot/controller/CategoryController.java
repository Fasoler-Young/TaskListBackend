package com.javabegin.tasklist.backendspringboot.controller;

import com.javabegin.tasklist.backendspringboot.entity.CategoryEntity;
import com.javabegin.tasklist.backendspringboot.exсeption.MissedOrRedundantParamException;
import com.javabegin.tasklist.backendspringboot.search.CategorySearchValues;
import com.javabegin.tasklist.backendspringboot.service.CategoryService;
import com.javabegin.tasklist.backendspringboot.service.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController extends SupportMethods{

    // TODO Продумать как во все контроллеры добавить определение текущего пользователя
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(UserDetailsServiceImpl userDetailsService, CategoryService categoryService) {
        super(userDetailsService);
        this.categoryService = categoryService;
    }


    @PostMapping("/add")
    @Operation(summary = "Add new category")
    public ResponseEntity<CategoryEntity> addCategory(@RequestParam String title) throws MissedOrRedundantParamException {

        if(isNullOrEmpty(title)){
            throw new MissedOrRedundantParamException("missed param: title");
        }
        return ResponseEntity.ok(categoryService.add(new CategoryEntity(title, getCurrentUserId())));
    }


    @PutMapping("/update")
    @Operation(summary = "Update category")
    public ResponseEntity<CategoryEntity> update(@RequestParam Integer id, @RequestParam String title) throws MissedOrRedundantParamException {
         if( id == null || id == 0){
             throw new MissedOrRedundantParamException("redundant param: id must be null");
         }
         if(isNullOrEmpty(title)){
             throw new MissedOrRedundantParamException("missed param: title");
         }
         // Здесь используется метод find вместо getById потому что нужно запретить пользователю менять чужие задачи
         // Это можно было бы исправить еще одним запросом, но это выглядит бессмысленно
         CategoryEntity category = categoryService.findByIdAndUserId(id, getCurrentUserId());
         if(category == null){
             throw new MissedOrRedundantParamException("category not found");
         }
         category.setTitle(title);
         return ResponseEntity.ok(categoryService.update(category));
    }


    // TODO Подумать над тем, чтобы можно было получить только свои задачи или просто сделать метод админским
    @GetMapping("/id")
    @Operation(summary = "Get Category by id")
    public ResponseEntity<CategoryEntity> getById(@RequestParam Integer id) throws MissedOrRedundantParamException {
        try {
            CategoryEntity category = categoryService.findById(id);
            return ResponseEntity.ok(category);
        }catch (Exception e){
            e.printStackTrace();
            throw new MissedOrRedundantParamException("id = " + id + " not found for user: " + getCurrentUserLogin());
        }
    }


    @DeleteMapping("/delete")
    @Operation(summary = "Delete category by id for current user")
    public ResponseEntity<HttpStatus> deleteByIdForCurrentUser(@RequestParam Integer id) throws MissedOrRedundantParamException {
        try{
            categoryService.deleteByIdAndUserId(id, getCurrentUserId());
        }catch (Exception e){
            e.printStackTrace();
            throw new MissedOrRedundantParamException("id = " + id + " not found for user: " + getCurrentUserLogin());
        }
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }


    @GetMapping("/all")
    @Operation(summary = "Get all priorities for current user")
    public List<CategoryEntity> findAllForCurrentUser(){
        return categoryService.findAllByUserIdOrderById(getCurrentUserId());
    }


    @PostMapping("/search")
    @Operation(summary = "Search By title for current user")
    public ResponseEntity<List<CategoryEntity>> searchForCurrentUser(@RequestBody CategorySearchValues searchValues){
        return ResponseEntity.ok(categoryService.findByTitleAndUserId(searchValues.getText(), getCurrentUserId()));

    }

}
