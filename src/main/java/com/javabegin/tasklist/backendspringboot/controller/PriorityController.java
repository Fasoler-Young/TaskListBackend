package com.javabegin.tasklist.backendspringboot.controller;

import com.javabegin.tasklist.backendspringboot.entity.PriorityEntity;
import com.javabegin.tasklist.backendspringboot.exсeption.MissedOrRedundantParamException;
import com.javabegin.tasklist.backendspringboot.exсeption.Response;
import com.javabegin.tasklist.backendspringboot.search.PrioritySearchValues;
import com.javabegin.tasklist.backendspringboot.service.PriorityService;
import com.javabegin.tasklist.backendspringboot.service.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/priority")
@Tag(name = "Priority", description = "All operations with categories")
public class PriorityController extends SupportMethods{

    private final PriorityService service;

    public PriorityController(UserDetailsServiceImpl userDetailsService, PriorityService service) {
        super(userDetailsService);
        this.service = service;
    }

    @PostMapping("/add")
    @Operation(summary = "Add new Priority for current user",
            responses = {
                    @ApiResponse(description = "New priority",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PriorityEntity.class))),
                    @ApiResponse(responseCode = "406", description = "missed param: Title",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class)))
            })
    // FIXME Здесь существует возможность сохранять приоритеты с цветом null,
    //  вероятно следует на стороне бд это запретить
    public ResponseEntity<PriorityEntity> addPriority(@RequestParam @Parameter(description = "Name of new priority", example = "High") String title,
                                                      @RequestParam(required = false) @Parameter(description = "Color of new priority", example = "#ffffff") String color) throws MissedOrRedundantParamException {
        if(isNullOrEmpty(title)){
            throw new MissedOrRedundantParamException("missed param: title");
        }

        return ResponseEntity.ok(service.add(new PriorityEntity(title, color, getCurrentUserId())));
    }

    @GetMapping("/id")
    @Operation(summary = "Get Priority by id for current user",
            responses = {
                    @ApiResponse(description = "New priority",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PriorityEntity.class))),
                    @ApiResponse(responseCode = "406", description = "Priority not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class)))
            })
    public ResponseEntity<PriorityEntity> getByIdAndUserId(@RequestParam @Parameter(description = "Identifier of priority", example = "1") Integer id) throws MissedOrRedundantParamException {
        try {
            PriorityEntity priority = service.findByIdAndUserId(id, getCurrentUserId());
            return ResponseEntity.ok(priority);
        }catch (Exception e){
            e.printStackTrace();
            throw new MissedOrRedundantParamException("id = " + id + " not found for user: " + getCurrentUserLogin());
        }
    }

    @PutMapping("/update")
    @Operation(summary = "Update Priority for current user",
            responses = {
                    @ApiResponse(description = "New priority",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PriorityEntity.class))),
                    @ApiResponse(responseCode = "406", description = "Priority not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class)))
            })
    public ResponseEntity<PriorityEntity> update(@RequestParam @Parameter(description = "Identifier of priority", example = "1") Integer id,
                                                 @RequestParam @Parameter(description = "New title of priority", example = "New title") String title) throws MissedOrRedundantParamException {

        if(id == null || id == 0){
            throw new MissedOrRedundantParamException("missed param: id");
        }

        if(isNullOrEmpty(title)){
            throw new MissedOrRedundantParamException("missed param: title");
        }
        try {
            PriorityEntity priority = service.findById(id);
            return ResponseEntity.ok(service.update(priority));
        }catch (Exception e){
            throw new MissedOrRedundantParamException("id = " + id + " not found for user: " + getCurrentUserLogin());
        }
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete priority by id for current user",
            responses = {
                    @ApiResponse(description = "Success operation",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PriorityEntity.class))),
                    @ApiResponse(responseCode = "406", description = "Priority not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class)))
            })
    public ResponseEntity<Boolean> delete(@RequestParam @Parameter(description = "Identifier of priority", example = "1") Integer id) throws MissedOrRedundantParamException {
        try{
            service.deleteByIdAndUserId(id, getCurrentUserId());
        }catch (Exception e){
            e.printStackTrace();
            throw new MissedOrRedundantParamException("id = " + id + " not found for user:" + getCurrentUserLogin());
        }
        return ResponseEntity.ok(true);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all priorities for current user",
            responses = {
                    @ApiResponse(description = "All priorities for current user",
                            content = @Content(mediaType = "application/json",
                                   array = @ArraySchema(schema = @Schema(implementation = PriorityEntity.class))))
            })
    public List<PriorityEntity> findAll(){
        return service.findAllByUserId(getCurrentUserId());
    }


    // Поиск по любым параметрам PrioritySearchValues
    @PostMapping("/search")
    @Operation(summary = "Search by title for current user",
            responses = {
                    @ApiResponse(description = "All priorities for current user found by parameters",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = PriorityEntity.class)))),
            })
    public ResponseEntity<List<PriorityEntity>> search(@RequestBody @Parameter(description = "search in titles") PrioritySearchValues prioritySearchValues){
        return ResponseEntity.ok(service.findByTitle(prioritySearchValues.getTitle(), getCurrentUserId()));
    }
}
