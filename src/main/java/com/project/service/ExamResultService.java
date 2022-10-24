package com.project.service;

import com.project.entity.ExamResultEntity;
import com.project.request.ExamResultFilterRequest;
import com.project.request.ExamResultRequest;
import com.project.response.ExamResultResponse;

import java.util.Optional;

public interface ExamResultService extends BaseService<ExamResultResponse,ExamResultRequest> {
    Optional<ExamResultEntity> findById(Long id);

    ExamResultResponse submit(ExamResultRequest req);

    ExamResultResponse findExamsByParamNative(ExamResultFilterRequest req);

    ExamResultResponse findExamResultsUser(Integer page, Integer size);

}
