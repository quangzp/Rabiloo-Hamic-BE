package com.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionResultDto extends AbstractDto{
    private String content;
    private Integer point;
}
