package com.project.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ExamDto extends BaseDto {
    private String title;
    private String description;
    private String type;
    private String code;
    private Date startFrom;
    private Date endTo;
    private List<QuestionDto> questions;
    private List<Long> candidateIds;
}
