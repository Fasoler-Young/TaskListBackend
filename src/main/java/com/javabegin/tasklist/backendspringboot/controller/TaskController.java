package com.javabegin.tasklist.backendspringboot.controller;

import com.javabegin.tasklist.backendspringboot.entity.TaskEntity;
import com.javabegin.tasklist.backendspringboot.search.TaskSearchValues;
import com.javabegin.tasklist.backendspringboot.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {
    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @PostMapping("/add")
    @Operation(summary = "Add new Task")
    public ResponseEntity<TaskEntity> add(@RequestBody TaskEntity task){

        if(task.getId() != null && task.getId() != 0){
            return new ResponseEntity("redundant param: id must be null", HttpStatus.NOT_ACCEPTABLE);
        }
        if(task.getTitle() == null || task.getTitle().trim().length() == 0){
            return new ResponseEntity("missed param: Title", HttpStatus.NOT_ACCEPTABLE);
        }
        if(task.getCategory().getId() == null || task.getCategory().getId() == 0){
            return new ResponseEntity("missed param: Category", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(service.add(task));
    }

    @PutMapping("/update")
    @Operation(summary = "Update Task")
    public ResponseEntity<TaskEntity> update(@RequestBody TaskEntity task){

        if(task.getId() == null || task.getId() == 0){
            return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
        }
        if(task.getTitle() == null || task.getTitle().trim().length() == 0){
            return new ResponseEntity("missed param: Title", HttpStatus.NOT_ACCEPTABLE);
        }
        if(task.getCategory().getId() == null || task.getCategory().getId() == 0){
            return new ResponseEntity("missed param: Category", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(service.update(task));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete task by id")
    public ResponseEntity delete(@PathVariable Integer id){
        try{
            service.deleteById(id);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all task")
    public ResponseEntity<List<TaskEntity>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/id/{id}")
    @Operation(summary = " Find one by id")
    public ResponseEntity<TaskEntity> findByID(@PathVariable Integer id){
        TaskEntity task = null;
        try {
            task = service.findById(id);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity("id= " + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(task);
    }

    @PostMapping("/search")
    @Operation(summary = "Serach by all fields")
    public ResponseEntity<Page<TaskEntity>> search(@RequestBody TaskSearchValues values){

        String title = values.getTitle() != null ? values.getTitle() : null;

        Boolean completed = values.getCompleted() != null ? values.getCompleted() : null;

        Integer priorityId = values.getPriorityId() != null ? values.getPriorityId() : null;

        Integer categoryId = values.getCategoryId() != null ? values.getCategoryId() : null;

        String sortColumn = values.getSortColumn() != null ? values.getSortColumn() : null;
        String sortDirection = values.getSortDirection() != null ? values.getSortDirection() : null;

        Integer pageNumber = values.getPageNumber() != null ? values.getPageNumber() : null;
        Integer pageSize = values.getPageSize() != null ? values.getPageSize() : null;


        Sort.Direction direction = sortDirection == null || sortDirection.trim().length() == 0 ||
                sortDirection.trim().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Sort sort = Sort.by(direction, sortColumn);

        // Объект постраничности
        PageRequest pageRequest = PageRequest.of(values.getPageNumber(), values.getPageSize(), sort);


        return ResponseEntity.ok(service.findAllByParam(title, completed, priorityId, categoryId, pageRequest));
    }

}
