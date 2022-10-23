package com.project.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ExamResultDto extends BaseDto {
    private String uuid;
    private Integer points;
    private Date start_time;
    private Date end_time;
    private Long examId;
}
