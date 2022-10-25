package com.project.controller;

import com.project.request.ChangePasswordRequest;
import com.project.request.UserRequest;
import com.project.response.UserResponse;
import com.project.service.UserService;
import com.project.service.user.RegisterUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private RegisterUserService registerUserService;

    @GetMapping("/user/info")
    public UserResponse getUser(){
        return service.getCurrent();
    }

    @GetMapping("/admin/all-user")
    public UserResponse getUsers(@RequestParam(defaultValue = "1") Integer page,
                                 @RequestParam(defaultValue = "5") Integer size){
        return service.getAllUserNoneDeleted(page,size);
    }
    @PostMapping("/public/user-create")
    public UserResponse createUser(@Valid @RequestBody UserRequest request){
        return service.create(request);
    }

    @PutMapping("/user/update-user-info")
    public UserResponse updateUserInfo(@RequestBody UserRequest request){
        return service.updateUserInfo(request);
    }

    @PutMapping("/user/user-changing-password")
    public UserResponse changePassword(@Valid @RequestBody ChangePasswordRequest request){
        return service.changePassword(request);
    }

    @PutMapping("/user/user-reset-password")
    public UserResponse resetPassword(@RequestParam String userName){
        return null;
    }

    @DeleteMapping("/admin/user-deactivate")
    public UserResponse deactivate(@RequestParam Long id){
        return service.deactivate(id);
    }

    @PostMapping("/public/register")
    public String requestRegister(@RequestParam String email) {
        return registerUserService.requestRegister(email);
    }

    @PostMapping("/public/forgot-password")
    public String requestForgotPassword(@RequestParam String email) {
        return null;
    }
}
