package com.project.controller;

import com.project.request.NotificationRequest;
import com.project.response.NotificationResponse;
import com.project.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class NotificationController {

    @Autowired
    private NotificationService service;

    @GetMapping("/public/notifi-all")
    public NotificationResponse getAll(){
        return service.findAll();
    }

    @PostMapping("/admin/notifi-create")
    public NotificationResponse create(@RequestBody NotificationRequest request) {
        return service.create(request);
    }

    @DeleteMapping("/admin/notifi-delete")
    public NotificationResponse delete(@RequestParam Long id){
        return service.delete(id);
    }
}
