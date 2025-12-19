package com.skilluser.user.service;

import com.skilluser.user.dto.AddQuestionsRequest;
import com.skilluser.user.dto.AnswerDto;
import com.skilluser.user.dto.StartTestRequest;
import com.skilluser.user.dto.StartTestResponse;
import com.skilluser.user.model.psychomatrictest.PsychometricTest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface PsychometricService {

    public PsychometricTest createTest(PsychometricTest psychometricTest);
    Map<String,Object> getTests(Pageable pageable);
    public void addQuestions(AddQuestionsRequest req) ;

    public StartTestResponse startTest(StartTestRequest req);

    public Map<String, Object> submitTest(Long attemptId, Long userId, List<AnswerDto> answers) ;



    }
