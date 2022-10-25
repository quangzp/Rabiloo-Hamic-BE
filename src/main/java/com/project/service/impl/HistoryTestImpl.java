package com.project.service.impl;

import com.project.config.UserAuth;
import com.project.dto.ExamResultDto;
import com.project.entity.ExamEntity;
import com.project.entity.ExamResultEntity;
import com.project.response.ExamResultResponse;
import com.project.response.HistoryTestResponse;
import com.project.service.ExamResultService;
import com.project.service.ExamService;
import com.project.service.HistoryTestService;
import com.project.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HistoryTestImpl implements HistoryTestService {
    @Autowired
    private UserAuth userAuth;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UserService userService;

    @Autowired
    private ExamResultService examResultService;

    @Autowired
    private ExamService examService;

    @Override
    public HistoryTestResponse findAllByExamId(Long examId) {
        ExamEntity exam = examService.findAllInfoExamById(examId);

        return null;
    }

    @Override
    public HistoryTestResponse findAllByUser(Integer page, Integer size) {
        Page<ExamResultEntity> pages = examResultService.findExamResultsUser(page,size);

        var examResultEntities = pages.getContent();
        List<ExamResultDto> examResultDtos = new ArrayList<>();
        for (ExamResultEntity examResultEntity : examResultEntities) {
            ExamResultDto examResultDto = mapper.map(examResultEntity,ExamResultDto.class);
            //examResultDto.setTitleExam(examResultEntity.getExam().getTitle());
            examResultDtos.add(examResultDto);
        }

        ExamResultResponse response = new ExamResultResponse();
        response.setTotal(pages.getTotalElements());
        response.setDtos(examResultDtos);
        response.setMessage("ok");
        response.setStatusCode(HttpStatus.OK);

        //return response;
        return null;
    }
}
