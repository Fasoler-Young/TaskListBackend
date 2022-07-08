package com.javabegin.tasklist.backendspringboot.controller;

import com.javabegin.tasklist.backendspringboot.entity.CategoryEntity;
import com.javabegin.tasklist.backendspringboot.entity.PriorityEntity;
import com.javabegin.tasklist.backendspringboot.entity.TaskEntity;
import com.javabegin.tasklist.backendspringboot.exсeption.MissedOrRedundantParamException;
import com.javabegin.tasklist.backendspringboot.exсeption.Response;
import com.javabegin.tasklist.backendspringboot.search.TaskSearchValues;
import com.javabegin.tasklist.backendspringboot.service.CategoryService;
import com.javabegin.tasklist.backendspringboot.service.PriorityService;
import com.javabegin.tasklist.backendspringboot.service.TaskService;
import com.javabegin.tasklist.backendspringboot.service.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
@Tag(name = "Task", description = "All operations with tasks")
public class TaskController extends SupportMethods {
    private final TaskService taskService;
    private final CategoryService categoryService;
    private final PriorityService priorityService;

    public TaskController(UserDetailsServiceImpl userDetailsService, TaskService taskService, CategoryService categoryService, PriorityService priorityService) {
        super(userDetailsService);
        this.taskService = taskService;
        this.categoryService = categoryService;
        this.priorityService = priorityService;
    }

    @PostMapping("/add")
    @Operation(summary = "Add new Task",
            responses = {
                    @ApiResponse(description = "New task",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TaskEntity.class))),
                    @ApiResponse(responseCode = "406", description = "missed param: title",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class))),
                    @ApiResponse(responseCode = "406", description = "category or priority not found for current user",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class)))
            })
    public ResponseEntity<TaskEntity> add(@RequestParam @Parameter(description = "Summary of new task", example = "Go for a walk") String title,
                                          @RequestParam(required = false) @Parameter(description = "Task is done?", example = "true") Boolean completed,
                                          @RequestParam @Parameter(description = "Identify of the category", example = "1") Integer categoryId,
                                          @RequestParam(required = false) @Parameter(description = "Identify of the priority", example = "1") Integer priorityId) throws MissedOrRedundantParamException {

        if(isNullOrEmpty(title)){
            throw new MissedOrRedundantParamException("missed param: title");
        }
        try {
            Integer currentUserId = getCurrentUserId();
            PriorityEntity priority = priorityService.findByIdAndUserId(priorityId, currentUserId);
            CategoryEntity category = categoryService.findByIdAndUserId(categoryId, currentUserId);
            return ResponseEntity.ok(taskService.add(new TaskEntity(title, completed, category, priority, currentUserId)));
        }catch (Exception e){
            throw new MissedOrRedundantParamException("category or priority not found for current user: categoryId = " + categoryId + ", priorityId = " + priorityId + ", user: " + getCurrentUserLogin());
        }

    }

    @PutMapping("/update")
    @Operation(summary = "Update Task",
            responses = {
                    @ApiResponse(description = "Updated task",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TaskEntity.class))),
                    @ApiResponse(responseCode = "406", description = "missed param: taskId",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class))),
                    @ApiResponse(responseCode = "406", description = "task, category or priority not found for current user",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class)))
            })
    public ResponseEntity<TaskEntity> update(@RequestParam @Parameter(description = "Identify of task", example = "1") Integer taskId,
                                             @RequestParam(required = false) @Parameter(description = "New summary of the task", example = "Make cake") String title,
                                             @RequestParam(required = false) @Parameter(description = "Task is done?", example = "true") Boolean completed,
                                             @RequestParam(required = false) @Parameter(description = "New identify of category. If null, category won't be changed.", example = "1") Integer categoryId,
                                             @RequestParam(required = false) @Parameter(description = "New identify of priority. If null, priority will be null.", example = "1") Integer priorityId) throws MissedOrRedundantParamException {

        if(taskId == null || taskId == 0){
            throw new MissedOrRedundantParamException("missed param: taskId");
        }
        // TODO Здесь наглядный пример того, что следует использовать DTO, но...
        try {
            Integer currentUserId = getCurrentUserId();
            // Данные запросы обеспечивают защиту от багов (на фронте это и так не должно позволяться),
            // при этом лишний раз дергая бд
            TaskEntity task = taskService.findByIdAndUserId(taskId, currentUserId);
            PriorityEntity priority = null;
            if (priorityId != null && priorityId != 0){
                priority = priorityService.findByIdAndUserId(priorityId, currentUserId);
            }
            if(categoryId != null && categoryId != 0){
                CategoryEntity category = categoryService.findByIdAndUserId(categoryId, currentUserId);
                task.setCategory(category);
            }
            if (!isNullOrEmpty(title)){
                task.setTitle(title);
            }
            if(completed != null){
                task.setCompleted(completed);
            }

            task.setPriority(priority);

            return ResponseEntity.ok(taskService.update(task));
        }catch (Exception e){
            throw new MissedOrRedundantParamException("task, category or priority not found for current user: taskId = " + taskId + ", categoryId = " + categoryId + ", priorityId = " + priorityId + ", user: " + getCurrentUserLogin());
        }
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete task by id",
            responses = {
                    @ApiResponse(description = "New task",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TaskEntity.class))),
                    @ApiResponse(responseCode = "406", description = "missed param: taskId",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class))),
                    @ApiResponse(responseCode = "406", description = "task not found for current user",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class)))
            })
    public ResponseEntity<Boolean> delete(@RequestParam @Parameter(description = "Identify of deleting task", example = "1") Integer taskId) throws MissedOrRedundantParamException {
        // FIXME Очень похоже, что данная структура не работает и если и делать проверку, то до удаления
        if(taskId == null || taskId == 0){
            throw new MissedOrRedundantParamException("missed param: taskId");
        }
        try{
            taskService.deleteByIdAndUserId(taskId, getCurrentUserId());
        }catch (Exception e){
            e.printStackTrace();
            throw new MissedOrRedundantParamException("id = " + taskId + " not found for user:" + getCurrentUserLogin());
        }
        return ResponseEntity.ok(true);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all task",
            responses = {
                    @ApiResponse(description = "All tasks for current user",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = TaskEntity.class))))
            })
    public ResponseEntity<List<TaskEntity>> findAll(){
        return ResponseEntity.ok(taskService.findAll());
    }

    @GetMapping("/id")
    @Operation(summary = " Find one by id",
            responses = {
                    @ApiResponse(description = "New task",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TaskEntity.class))),
                    @ApiResponse(responseCode = "406", description = "missed param: taskId",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class))),
                    @ApiResponse(responseCode = "406", description = "task not found for current user",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class)))
            })
    public ResponseEntity<TaskEntity> findByID(@RequestParam Integer taskId) throws MissedOrRedundantParamException {
        // FIXME Метод никогда не выдает данное исключение
        try {
            TaskEntity task = taskService.findByIdAndUserId(taskId, getCurrentUserId());
            return ResponseEntity.ok(task);
        }catch (Exception e){
            e.printStackTrace();
            throw new MissedOrRedundantParamException("id = " + taskId + " not found for user:" + getCurrentUserLogin());
        }
    }

    @PostMapping("/search")
    @Operation(summary = "Search by all fields for current user",
            responses = {
                    @ApiResponse(description = "All tasks for current user found by parameters",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = TaskEntity.class)))),
            })
    public ResponseEntity<Page<TaskEntity>> search(@RequestBody TaskSearchValues values){

        String title = values.getTitle() != null ? values.getTitle() : null;

        Boolean completed = values.getCompleted() != null ? values.getCompleted() : null;

        Integer priorityId = values.getPriorityId() != null ? values.getPriorityId() : null;

        Integer categoryId = values.getCategoryId() != null ? values.getCategoryId() : null;

        String sortColumn = values.getSortColumn() != null ? values.getSortColumn() : null;
        String sortDirection = values.getSortDirection() != null ? values.getSortDirection() : null;

        Integer pageNumber = values.getPageNumber() != null ? values.getPageNumber() : 1;
        Integer pageSize = values.getPageSize() != null ? values.getPageSize() : 10;


        Sort.Direction direction = isNullOrEmpty(sortDirection) ||
                sortDirection.trim().equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Sort sort = Sort.by(direction, sortColumn);

        // Объект постраничности
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);


        return ResponseEntity.ok(taskService.findAllByParam(title, completed, priorityId, categoryId, pageRequest));
    }

}
