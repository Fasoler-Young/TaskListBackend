package com.javabegin.tasklist.backendspringboot.controller;

import com.javabegin.tasklist.backendspringboot.entity.UserEntity;
import com.javabegin.tasklist.backendspringboot.exсeption.MissedOrRedundantParamException;
import com.javabegin.tasklist.backendspringboot.service.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/registration")
public class RegistrationController {

    private final UserDetailsServiceImpl userDetailsService;


    public RegistrationController(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/user")
    @Operation(summary = "Add new user")
    public ResponseEntity<Boolean> registrationUser(@RequestBody UserEntity user) throws MissedOrRedundantParamException {
        if(user.getId() != null && user.getId() != 0){
            throw new MissedOrRedundantParamException("redundant param: id must be null");
        }
        if(userDetailsService.existByLogin(user.getLogin())){
            throw new MissedOrRedundantParamException("login already in use");
        }
        user.setRole("ROLE_USER");
        userDetailsService.add(user);

        return ResponseEntity.ok(true);
    }

    @PostMapping("/admin")
    @Operation(summary = "Add new admin")
    public ResponseEntity<Boolean> registrationAdmin(@RequestBody UserEntity user) throws MissedOrRedundantParamException {
        if(user.getId() != null && user.getId() != 0){
            throw new MissedOrRedundantParamException("redundant param: id must be null");
        }
        if(userDetailsService.existByLogin(user.getLogin())){
            throw new MissedOrRedundantParamException("login already in use");
        }
        user.setRole("ROLE_ADMIN");
        userDetailsService.add(user);

        return ResponseEntity.ok(true);
    }

    // TODO Сделать обработку исключений при удалении
    @DeleteMapping("/deleteYourself")
    @Operation(summary = "Delete current user")
    public ResponseEntity<Boolean> deleteYourself() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityContextHolder.clearContext();
        userDetailsService.deleteByLogin(authentication.getName());
        return ResponseEntity.ok(true);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all users")
    public ResponseEntity<List<UserEntity>> findAll(){
        return ResponseEntity.ok(userDetailsService.findAll());
    }

    @PutMapping("/updatePassword")
    @Operation(summary = "Change password for current user")
    public ResponseEntity<Boolean> changePassword(@RequestParam String password) throws MissedOrRedundantParamException {

        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        if(password == null || password.trim().length() == 0){
            throw new MissedOrRedundantParamException("missed param: password");
        }
        userDetailsService.changePassword(login, password);
        return ResponseEntity.ok(true);
    }

    @PutMapping("/updateRole")
    @Operation(summary = "Change role for any user")
    public ResponseEntity<Boolean> changeRole(@RequestParam String login, @RequestParam String role) throws MissedOrRedundantParamException {
        if(login == null || login.trim().length() == 0){
            throw new MissedOrRedundantParamException("missed param: password");
        }
        if(!userDetailsService.existByLogin(login)){
            throw new MissedOrRedundantParamException(("login " + login + " not found"));
        }
        if(role == null || role.trim().length() == 0){
            throw new MissedOrRedundantParamException("missed param: role");
        }
        userDetailsService.changeRole(login, role);
        return ResponseEntity.ok(true);
    }

    @DeleteMapping("/deleteByLogin")
    @Operation(summary = "Delete user by login")
    public ResponseEntity<Boolean> deleteByLogin(@RequestParam String login) throws MissedOrRedundantParamException {
        // TODO Вероятно, нужно сделать общую функцию с проверкой на корректность строк, чисел и тд.
        if(login == null || login.trim().length() == 0){
            throw new MissedOrRedundantParamException("missed param: password");
        }
        // TODO Возможно не следует выполнять отдельный запрос в бд, а сделать счетчик удаленных строк
        if(!userDetailsService.existByLogin(login)){
            throw new MissedOrRedundantParamException(("login " + login + " not found"));
        }
        userDetailsService.deleteByLogin(login);
        // TODO Нужно продумать насчет возвращаемых значений
        return ResponseEntity.ok(true);
    }

}
