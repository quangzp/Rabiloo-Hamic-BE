package com.project.controller;

import com.project.response.HistoryTestResponse;
import com.project.service.HistoryTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HistoryTest {

    @Autowired
    private HistoryTestService service;

    @GetMapping("/user/exams-history-2")
    public HistoryTestResponse getHistoryTests(@RequestParam(defaultValue = "1") Integer page,
                                              @RequestParam(defaultValue = "5") Integer size){
        return service.findAllByUser(page,size);
    }
}
