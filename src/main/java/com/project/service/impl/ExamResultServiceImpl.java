package com.project.service.impl;

import com.project.dto.ExamResultDto;
import com.project.dto.QuestionResultDto;
import com.project.entity.ExamEntity;
import com.project.entity.ExamResultEntity;
import com.project.repository.ExamResultRepository;
import com.project.request.ExamResultFilterRequest;
import com.project.request.ExamResultRequest;
import com.project.response.ExamResultResponse;
import com.project.security.CustomUserDetail;
import com.project.service.ExamResultService;
import com.project.service.ExamService;
import com.project.service.QuestionResultService;
import com.project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ExamResultServiceImpl implements ExamResultService {

    @Autowired
    private ExamResultRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    private ExamService examService;

    private QuestionResultService questionResultService;
    @Autowired
    public ExamResultServiceImpl(@Lazy QuestionResultService questionResultService) {
        this.questionResultService = questionResultService;
    }

    @Autowired
    private ModelMapper mapper;


    @Override
    public ExamResultResponse findAll() {
        List<ExamResultEntity> entities = repository.findAll();

        ExamResultResponse response = new ExamResultResponse();
        if(entities.isEmpty()){
            response.setMessage("Exams not found");
            response.setStatusCode(HttpStatus.NOT_FOUND);
        }else {
            List<ExamResultDto> dtos = new ArrayList<>();
            entities.forEach(e -> dtos.add(mapper.map(e, ExamResultDto.class)));

            response.setDtos(dtos);
            response.setMessage("OK");
            response.setStatusCode(HttpStatus.OK);
        }

        return response;
    }

    @Override
    public ExamResultResponse findOne(Long id) {
        Optional<ExamResultEntity> examResultEntityOptional = repository.findById(id);

        ExamResultResponse response = new ExamResultResponse();
        if(!examResultEntityOptional.isPresent()){
            response.setMessage("exam not found");
            response.setStatusCode(HttpStatus.NOT_FOUND);
        }else {
            ExamResultDto dto = mapper.map(examResultEntityOptional.get(),ExamResultDto.class);
            response.setDto(dto);
            response.setMessage("OK");
            response.setStatusCode(HttpStatus.OK);
        }

        return response;
    }

    @Override
    public ExamResultResponse create(ExamResultRequest req) {
        req.setStart_time(new Date().getTime());
        return save(req);
    }

    @Override
    public ExamResultResponse update(ExamResultRequest req) {
        Optional<ExamResultEntity> examResultEntityOptional = repository.findById(req.getId());

        ExamResultResponse response = new ExamResultResponse();
        if(!examResultEntityOptional.isPresent()){
            response.setMessage("Exam result is not found");
            response.setStatusCode(HttpStatus.NOT_FOUND);
            return response;
        }

        if(req.getUserId() != null){
            if(!req.getUserId().equals(examResultEntityOptional.get().getUser().getId())){
                response.setMessage("User is not true");
                response.setStatusCode(HttpStatus.NOT_FOUND);
                return response;
            }
        }

        return save(req);
    }

    private ExamResultResponse save(ExamResultRequest req) {
        Optional<ExamEntity> examEntityOptional = examService.findById(req.getExamId());

        ExamResultResponse response = new ExamResultResponse();
        if(!examEntityOptional.isPresent()){
            response.setMessage("exam is not found");
            response.setStatusCode(HttpStatus.NOT_FOUND);
            return response;
        }

        ExamResultEntity entity = mapper.map(req,ExamResultEntity.class);
        entity.setExam(examEntityOptional.get());

        /*if(req.getUserId() != null){
            Optional<UserEntity> userEntityOptional = userService.findById(req.getUserId());
            if(!userEntityOptional.isPresent()){
                response.setMessage("User is not true");
                response.setStatusCode(HttpStatus.NOT_FOUND);
                return response;
            }
            entity.setUser(userEntityOptional.get());
        }*/

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String userName = ((CustomUserDetail) authentication.getPrincipal()).getUsername();
            entity.setUser(userService.findByUserName(userName));
        }


        ExamResultDto dto = mapper.map(repository.save(entity),ExamResultDto.class);
        response.setDto(dto);
        response.setMessage("OK");
        response.setStatusCode(HttpStatus.OK);

        return response;
    }

    @Override
    public ExamResultResponse delete(Long id) {
        Optional<ExamResultEntity> optional = repository.findById(id);

        ExamResultResponse response = new ExamResultResponse();
        if (!optional.isPresent()) {
            response.setMessage("Exam is not found");
            response.setStatusCode(HttpStatus.NOT_FOUND);
        } else {
            ExamResultEntity entity = optional.get();
            entity.setDeleted(true);
            ExamResultDto dto = mapper.map(repository.save(entity), ExamResultDto.class);

            response.setMessage("OK");
            response.setStatusCode(HttpStatus.OK);
        }

        return response;
    }

    @Override
    public Optional<ExamResultEntity> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public ExamResultResponse submit(ExamResultRequest req) {
        Optional<ExamResultEntity> examResultEntityOptional = repository.findById(req.getId());

        ExamResultResponse response = new ExamResultResponse();
        if(!examResultEntityOptional.isPresent()){
            response.setMessage("exam is not found");
            response.setStatusCode(HttpStatus.NOT_FOUND);
            return response;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String userName = ((CustomUserDetail) authentication.getPrincipal()).getUsername();
            if(!userName.equals(examResultEntityOptional.get().getUser().getUserName())){
                response.setMessage("User is not true");
                response.setStatusCode(HttpStatus.NOT_FOUND);
                return response;
            }
        }

        try {
            List<QuestionResultDto> questionResultDtos = questionResultService.saveAll(req.getQuestionResultRequests());

            int totalPoint = 0;
            for (QuestionResultDto questionResultDto : questionResultDtos) {
                if(questionResultDto.getPoint() != null)
                    totalPoint+=questionResultDto.getPoint();
            }
            req.setPoints(totalPoint);

            req.setEnd_time(new Date().getTime());

            return save(req);
        }catch (Exception e){
            throw e;
            /*response.setMessage("Something wrong");
            response.setStatusCode(HttpStatus.BAD_REQUEST);
            return response;*/
        }
    }

    @Override
    public ExamResultResponse findExamsByParamNative(ExamResultFilterRequest req) {
        List<ExamResultEntity> entities = repository.findExamResultsByParamNative(req.getExamId(),
                req.getMinPoint(), req.getMaxPoint(), req.getLongTime());

        ExamResultResponse response = new ExamResultResponse();
        if(entities.isEmpty()){
            response.setMessage("List is empty");
            response.setStatusCode(HttpStatus.NOT_FOUND);
        }else {
            List<ExamResultDto> dtos = new ArrayList<>();
            entities.forEach(e -> dtos.add(mapper.map(e,ExamResultDto.class)));

            response.setDtos(dtos);
            response.setMessage("OK");
            response.setStatusCode(HttpStatus.OK);
        }

        return response;
    }

}
