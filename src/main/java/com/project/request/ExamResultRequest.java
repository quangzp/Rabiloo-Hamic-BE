package com.project.request;

import lombok.Data;

import java.util.List;

@Data
public class ExamResultRequest {
    private Long id; // id of exam_result
    private String uuid;
    private Integer points;
    private Long examId;
    private Long userId;
    private boolean isSubmit;
    private Long endTime;
    private List<QuestionResultRequest> questionResultRequests;
}
