package com.project.service;

import com.project.entity.ExamEntity;
import com.project.request.ExamFilterRequest;
import com.project.request.ExamRequest;
import com.project.response.ExamResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public interface ExamService extends BaseService<ExamResponse, ExamRequest> {

    ExamEntity findById(Long examId);

    ExamResponse findPublicExam(Long id);

    ExamResponse findExamsByParamNative(ExamFilterRequest request);

    ExamResponse findPublicExams();

    ByteArrayInputStream genExamExcelFile(Long examId);

    void createExamFromExcelFile(Long examId, MultipartFile file) throws IOException;

    ExamEntity findAllInfoExamById(Long examId);

}
