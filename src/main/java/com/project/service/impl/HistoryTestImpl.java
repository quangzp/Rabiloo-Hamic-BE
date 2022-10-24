package com.project.service.impl;

import com.project.entity.ExamEntity;
import com.project.response.HistoryTestResponse;
import com.project.service.ExamResultService;
import com.project.service.ExamService;
import com.project.service.HistoryTestService;
import com.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoryTestImpl implements HistoryTestService {

    @Autowired
    private UserService userService;

    @Autowired
    private ExamResultService examResultService;

    @Autowired
    private ExamService examService;

    @Override
    public HistoryTestResponse findAllByExam(Long examId) {
        ExamEntity exam = examService.findAllInfoExamById(examId);



        return null;
    }
}
