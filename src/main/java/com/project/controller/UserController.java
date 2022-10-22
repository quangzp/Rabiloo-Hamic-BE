package com.project.controller;

import com.project.request.UserRequest;
import com.project.response.UserResponse;
import com.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/user/info")
    public UserResponse getUser(){
        return service.getCurrent();
    }
    @PostMapping("/public/user-create")
    public UserResponse createUser(@Valid @RequestBody UserRequest request){
        return service.create(request);
    }

    @PutMapping("/user/update-user-info")
    public UserResponse updateUserInfo(@RequestBody UserRequest request){
        return service.updateUserInfo(request);
    }
}
