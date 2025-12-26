package com.skilluser.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.skilluser.user.dto.AddQuestionsRequest;
import com.skilluser.user.dto.AnswerDto;
import com.skilluser.user.dto.StartTestRequest;
import com.skilluser.user.dto.StartTestResponse;
import com.skilluser.user.model.psychomatrictest.PsychometricTest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface PsychometricService {

    PsychometricTest createTest(PsychometricTest psychometricTest);

    Map<String, Object> getTests(Pageable pageable);

    void addQuestions(AddQuestionsRequest req);

    StartTestResponse startTest(StartTestRequest req);

    Map<String, Object> submitTest(Long attemptId, Long userId, List<AnswerDto> answers);

    List<Map<String, Object>> getSummary(Long userId) throws JsonProcessingException;
}
