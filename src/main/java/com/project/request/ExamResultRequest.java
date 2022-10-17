package com.project.request;

import lombok.Data;

import java.util.List;

@Data
public class ExamResultRequest {
    private Long id; // id of exam_result
    private Integer points;
    private Long start_time;
    private Long end_time;
    private Long examId;
    private Long userId;
    private List<QuestionResultRequest> questionResultRequests;
}
