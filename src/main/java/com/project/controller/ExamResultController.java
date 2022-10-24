package com.project.controller;

import com.project.request.ExamResultFilterRequest;
import com.project.request.ExamResultRequest;
import com.project.response.ExamResultResponse;
import com.project.service.ExamResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ExamResultController {
    @Autowired
    private ExamResultService service;

    //TODO create api start exam:
    // + save start time

    @GetMapping("/user/exams-history")
    public ExamResultResponse getExamsHistory(@RequestParam(defaultValue = "1") Integer page,
                                              @RequestParam(defaultValue = "5") Integer size) {
        return service.findExamResultsUser(page,size);
    }
    @PostMapping("/public/exam-start")
    public ExamResultResponse startExam(@RequestBody ExamResultRequest req) {
        return service.create(req);
    }

    @PutMapping("/public/exam-submit")
    public ExamResultResponse submitExam(@RequestBody ExamResultRequest req) {
        return service.submit(req);
    }


    //TODO create api filter exam result
    // filter by exam id, `points in range`, ...
    @PostMapping("/public/exam-result-filter")
    public ExamResultResponse getExamsResultByParamRequest(@RequestBody ExamResultFilterRequest req) {
        return service.findExamsByParamNative(req);
    }
}
