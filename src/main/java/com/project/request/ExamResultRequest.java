package com.project.request;

import lombok.Data;

@Data
public class ExamResultRequest {
    private Long id; // id of exam_result
    private String uuid;
    private Integer points;
    private Long start_time;
    private Long end_time;
    private Long examId;
    private Long userId;
    private boolean isSubmit = false;
    //private List<QuestionResultRequest> questionResultRequests;
}
