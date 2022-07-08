package com.javabegin.tasklist.backendspringboot.controller;

import com.javabegin.tasklist.backendspringboot.entity.StatEntity;
import com.javabegin.tasklist.backendspringboot.service.StatService;
import com.javabegin.tasklist.backendspringboot.service.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Stat", description = "Get statistic")
public class StatController extends SupportMethods {

    private final StatService service;

    public StatController(UserDetailsServiceImpl userDetailsService, StatService service) {
        super(userDetailsService);
        this.service = service;
    }

    @GetMapping("/stat")
    @Operation(summary = "Get full statistic for current user",
            responses = {
                    @ApiResponse(description = "Statistic for current user",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = StatEntity.class)))
            })
    public ResponseEntity<StatEntity> findByUserId(){
        return ResponseEntity.ok(service.findByUserId(getCurrentUserId()));
    }

    @GetMapping("/stat/all")
    @Operation(summary = "Get statistic for all users",
            responses = {
                @ApiResponse(description = "Full statistic",
                    content = @Content(mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = StatEntity.class))))
            })
    public ResponseEntity<List<StatEntity>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

}
