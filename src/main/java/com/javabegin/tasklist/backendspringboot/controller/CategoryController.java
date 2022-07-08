package com.javabegin.tasklist.backendspringboot.controller;

import com.javabegin.tasklist.backendspringboot.entity.CategoryEntity;
import com.javabegin.tasklist.backendspringboot.exсeption.MissedOrRedundantParamException;
import com.javabegin.tasklist.backendspringboot.exсeption.Response;
import com.javabegin.tasklist.backendspringboot.search.CategorySearchValues;
import com.javabegin.tasklist.backendspringboot.service.CategoryService;
import com.javabegin.tasklist.backendspringboot.service.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@Tag(name = "Category", description = "All operations with categories")
public class CategoryController extends SupportMethods{

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(UserDetailsServiceImpl userDetailsService, CategoryService categoryService) {
        super(userDetailsService);
        this.categoryService = categoryService;
    }


    @PostMapping("/add")
    @Operation(summary = "Add new category",
            responses = {
                @ApiResponse(description = "New category",
                    content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = CategoryEntity.class))),
                @ApiResponse(responseCode = "406", description = "missed param: title",
                    content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Response.class)))
            })
    public ResponseEntity<CategoryEntity> addCategory(@RequestParam @Parameter(description = "Name of new category", example = "University") String title) throws MissedOrRedundantParamException {

        if(isNullOrEmpty(title)){
            throw new MissedOrRedundantParamException("missed param: title");
        }
        return ResponseEntity.ok(categoryService.add(new CategoryEntity(title, getCurrentUserId())));
    }


    @PutMapping("/update")
    @Operation(summary = "Update category",
            responses = {
                    @ApiResponse(description = "Updated category",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CategoryEntity.class))),
                    @ApiResponse(responseCode = "406", description = "missed or redundant param",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class)))
            })
    public ResponseEntity<CategoryEntity> update(@RequestParam @Parameter(description = "Identifier of category", example = "1") Integer id,
                                                 @RequestParam @Parameter(description = "New title of category", example = "New title") String title) throws MissedOrRedundantParamException {
         if( id == null || id == 0){
             throw new MissedOrRedundantParamException("missed param: id");
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


    @GetMapping("/id")
    @Operation(summary = "Get category by id",
            responses = {
                    @ApiResponse(description = "Category found by id",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CategoryEntity.class))),
                    @ApiResponse(responseCode = "406", description = "Category not found for current user",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class)))
            })
    public ResponseEntity<CategoryEntity> getByIdAndUserId(@RequestParam @Parameter(description = "Identifier of category", example = "1") Integer id) throws MissedOrRedundantParamException {
        try {
            CategoryEntity category = categoryService.findByIdAndUserId(id, getCurrentUserId());
            return ResponseEntity.ok(category);
        }catch (Exception e){
            e.printStackTrace();
            throw new MissedOrRedundantParamException("id = " + id + " not found for user: " + getCurrentUserLogin());
        }
    }


    @DeleteMapping("/delete")
    @Operation(summary = "Delete category by id for current user",
            responses = {
                    @ApiResponse(description = "Result status",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Boolean.class))),
                    @ApiResponse(responseCode = "406", description = "Category not found for current user",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class)))
            })
    public ResponseEntity<Boolean> deleteByIdForCurrentUser(@RequestParam @Parameter(description = "Identifier of category", example = "1") Integer id) throws MissedOrRedundantParamException {
        try{
            categoryService.deleteByIdAndUserId(id, getCurrentUserId());
        }catch (Exception e){
            e.printStackTrace();
            throw new MissedOrRedundantParamException("id = " + id + " not found for user: " + getCurrentUserLogin());
        }
        return ResponseEntity.ok(true);
    }


    @GetMapping("/all")
    @Operation(summary = "Get all priorities for current user",
            responses = {
                    @ApiResponse(description = "All categories for current user",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = CategoryEntity.class)))),
            })
    public List<CategoryEntity> findAllForCurrentUser(){
        return categoryService.findAllByUserIdOrderById(getCurrentUserId());
    }


    @PostMapping("/search")
    @Operation(summary = "Search by title for current user",
            responses = {
                    @ApiResponse(description = "All categories for current user found by parameters",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = CategoryEntity.class)))),
            })
    public ResponseEntity<List<CategoryEntity>> searchForCurrentUser(@RequestBody @Parameter(description = "search in titles") CategorySearchValues searchValues){
        return ResponseEntity.ok(categoryService.findByTitleAndUserId(searchValues.getTitle(), getCurrentUserId()));

    }

}
