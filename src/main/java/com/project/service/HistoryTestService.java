package com.project.service;

import com.project.response.HistoryTestResponse;

public interface HistoryTestService {
    HistoryTestResponse findAllByExamId(Long examId);

    HistoryTestResponse findAllByUser(Integer page, Integer size);
}
