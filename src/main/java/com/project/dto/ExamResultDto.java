package com.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamResultDto extends BaseDto {
    private String titleExam;
    private Integer points;
    private Long start;
    private Long end;
    private Long examId;
}
