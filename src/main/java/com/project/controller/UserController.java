package com.project.controller;

import com.project.request.UserRequest;
import com.project.response.UserResponse;
import com.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/user/details")
    public UserResponse getUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return null;
    }
    @PostMapping("/admin/user-create")
    public UserResponse createUser(@RequestBody UserRequest request){
        return service.create(request);
    }

    @PutMapping("/admin/update-user-info")
    public UserResponse updateUserInfo(@RequestBody UserRequest request){
        return service.updateUserInfo(request);
    }
}
