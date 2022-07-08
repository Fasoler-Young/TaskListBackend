package com.javabegin.tasklist.backendspringboot.controller;

import com.javabegin.tasklist.backendspringboot.entity.PriorityEntity;
import com.javabegin.tasklist.backendspringboot.entity.UserEntity;
import com.javabegin.tasklist.backendspringboot.exсeption.MissedOrRedundantParamException;
import com.javabegin.tasklist.backendspringboot.exсeption.Response;
import com.javabegin.tasklist.backendspringboot.service.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.postgresql.util.PSQLException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PersistenceException;
import java.util.List;


@RestController
@RequestMapping("/registration")
@Tag(name = "User", description = "All operations with users and registration")
public class RegistrationController extends SupportMethods {

// TODO Возможно стоит сделать userDto,
//  чтобы возвращать значения при создании пользователей,
//  либо Возвращать Boolean

    public RegistrationController(UserDetailsServiceImpl userDetailsService) {
        super(userDetailsService);
    }



    @PostMapping("/user")
    @Operation(summary = "Add new user",
            responses = {
                    @ApiResponse(description = "Operation success",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Boolean.class))),
                    @ApiResponse(responseCode = "406", description = "missed param: login or password",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class))),
                    @ApiResponse(responseCode = "406", description = "login already in use",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class)))
            })
    public ResponseEntity<Boolean> registrationUser(@RequestParam @Parameter(description = "Name of new user", example = "User") String login,
                                                    @RequestParam @Parameter(description = "Secret password") String password) throws MissedOrRedundantParamException {
        if(isNullOrEmpty(login)){
            throw new MissedOrRedundantParamException("Missed param: login");
        }
        if(isNullOrEmpty(password)){
            throw new MissedOrRedundantParamException("missed param: password");
        }
        if(getUserDetailsService().existByLogin(login)){
            throw new MissedOrRedundantParamException("login already in use");
        }
        getUserDetailsService().add(new UserEntity(login, password, "ROLE_USER"));
        return ResponseEntity.ok(true);
    }

    @PostMapping("/admin")
    @Operation(summary = "Add new admin",
            responses = {
                    @ApiResponse(description = "Operation success",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Boolean.class))),
                    @ApiResponse(responseCode = "406", description = "missed param: login or password",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class))),
                    @ApiResponse(responseCode = "406", description = "login already in use",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class)))
            })
    public ResponseEntity<Boolean> registrationAdmin(@RequestParam @Parameter(description = "Name of new admin", example = "Admin") String login,
                                                     @RequestParam @Parameter(description = "Secret password") String password) throws MissedOrRedundantParamException {

        if(isNullOrEmpty(login)){
            throw new MissedOrRedundantParamException("Missed param: login");
        }
        if(isNullOrEmpty(password)){
            throw new MissedOrRedundantParamException("missed param: password");
        }
        if(getUserDetailsService().existByLogin(login)){
            throw new MissedOrRedundantParamException("login already in use");
        }
        getUserDetailsService().add(new UserEntity(login, password, "ROLE_ADMIN"));
        return ResponseEntity.ok(true);
    }

    @DeleteMapping("/deleteYourself")
    @Operation(summary = "Delete current user",
            responses = {
                    @ApiResponse(description = "Operation success",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Boolean.class))),
            })
    public ResponseEntity<Boolean> deleteYourself() {
        getUserDetailsService().deleteByLogin(getCurrentUserLogin());
        return ResponseEntity.ok(true);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all users",
            responses = {
                    @ApiResponse(description = "List of all users",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = UserEntity.class)))),
            })
    public ResponseEntity<List<UserEntity>> findAll(){
        return ResponseEntity.ok(getUserDetailsService().findAll());
    }

    @PutMapping("/updatePassword")
    @Operation(summary = "Change password for current user",
            responses = {
                    @ApiResponse(description = "Operation success",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Boolean.class))),
                    @ApiResponse(responseCode = "406", description = "missed param: login or password",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class))),
            })
    public ResponseEntity<Boolean> changePassword(@RequestParam @Parameter(description = "Secret code") String password) throws MissedOrRedundantParamException {
        if(isNullOrEmpty(password)){
            throw new MissedOrRedundantParamException("missed param: password");
        }
        getUserDetailsService().changePassword(getCurrentUserLogin(), password);
        return ResponseEntity.ok(true);
    }

    @PutMapping("/updateRole")
    @Operation(summary = "Change role for any user",
            responses = {
                    @ApiResponse(description = "Operation success",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Boolean.class))),
                    @ApiResponse(responseCode = "406", description = "missed param: login or password",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class))),

            })
    public ResponseEntity<Boolean> changeRole(@RequestParam @Parameter(description = "Name of the user being modified", example = "User") String login,
                                              @RequestParam @Parameter(description = "New role", example = "ROLE_USER") String role) throws MissedOrRedundantParamException {
        // Если прислать null все равно отработает, но проверка добавляет красоты
        if(isNullOrEmpty(login)){
            throw new MissedOrRedundantParamException("missed param: login");
        }

        if(!getUserDetailsService().existByLogin(login)){
            throw new MissedOrRedundantParamException(("login " + login + " not found"));
        }
        // Здесь нет проверки на существование роли, но в бд зашиты конкретные значения
        // так что не стоит множить проверки
        if(isNullOrEmpty(role)){
            throw new MissedOrRedundantParamException("missed param: role");
        }
        try {
            getUserDetailsService().changeRole(login, role);
        }catch (PersistenceException e){
            throw new MissedOrRedundantParamException("role is not correct");
        }
        return ResponseEntity.ok(true);
    }

    @DeleteMapping("/deleteByLogin")
    @Operation(summary = "Delete user by login",
            responses = {
                    @ApiResponse(description = "Operation success",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Boolean.class))),
                    @ApiResponse(responseCode = "406", description = "missed param: login",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class))),
            })
    public ResponseEntity<Boolean> deleteByLogin(@RequestParam @Parameter(description = "Name of the deleting user", example = "User") String login) throws MissedOrRedundantParamException {
        if(isNullOrEmpty(login)){
            throw new MissedOrRedundantParamException("missed param: login");
        }
        // Все работает, но может просто сделать счетчик, чтобы лишний раз не обращаться к бд
        if(!getUserDetailsService().existByLogin(login)){
            throw new MissedOrRedundantParamException(("login " + login + " not found"));
        }
        getUserDetailsService().deleteByLogin(login);
        // TODO Нужно продумать насчет возвращаемых значений
        return ResponseEntity.ok(true);
    }

}
