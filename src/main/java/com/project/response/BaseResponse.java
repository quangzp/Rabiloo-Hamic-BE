package com.project.response;

import com.project.dto.AbstractDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
public class BaseResponse<T extends AbstractDto> {
    private String message;
    private HttpStatus statusCode;
    private T dto;
    private List<T> dtos;


}
