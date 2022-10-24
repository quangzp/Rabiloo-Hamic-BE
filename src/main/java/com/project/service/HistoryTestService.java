package com.project.service;

import com.project.response.HistoryTestResponse;

public interface HistoryTestService {
    HistoryTestResponse findAllByExam(Long examId);
}
