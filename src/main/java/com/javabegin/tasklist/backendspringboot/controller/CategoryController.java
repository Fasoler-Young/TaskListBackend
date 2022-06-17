package com.javabegin.tasklist.backendspringboot.controller;

import com.javabegin.tasklist.backendspringboot.entity.CategoryEntity;
import com.javabegin.tasklist.backendspringboot.entity.PriorityEntity;
import com.javabegin.tasklist.backendspringboot.repo.CategoryRepository;
import com.javabegin.tasklist.backendspringboot.search.CategorySearchValues;
import com.javabegin.tasklist.backendspringboot.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
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

        return ResponseEntity.ok(service.add(category));
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



        return ResponseEntity.ok(service.update(category));

    }

    @GetMapping("/id/{id}")
    @Operation(summary = "Get Category by id")
    public ResponseEntity<CategoryEntity> getById(@PathVariable Integer id){
        CategoryEntity category = null;
        try {
            category = service.findById(id);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity("id = " + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete category by id")
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
    public List<CategoryEntity> findAll(){
        return service.findAllByOrderById();
    }

    @PostMapping("/serach")
    @Operation(summary = "Search By title")
    public ResponseEntity<List<CategoryEntity>> search(@RequestBody CategorySearchValues searchValues){
        return ResponseEntity.ok(service.findByTitle(searchValues.getText()));

    }

}
