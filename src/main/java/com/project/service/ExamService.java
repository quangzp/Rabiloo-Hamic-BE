package com.project.service;

import com.project.entity.ExamEntity;
import com.project.request.ExamFilterRequest;
import com.project.request.ExamRequest;
import com.project.response.ExamResponse;

import java.io.ByteArrayInputStream;
import java.util.Optional;

public interface ExamService extends BaseService<ExamResponse,ExamRequest>{

    Optional<ExamEntity> findById(Long examId);

    ExamResponse findPublicExam(Long id);

    ExamResponse findExamsByParamNative(ExamFilterRequest request);


    ExamResponse findPublicExams();


    ByteArrayInputStream genExamExcelFile(Long examId);
}
