package com.skilluser.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.skilluser.user.dto.AddQuestionsRequest;
import com.skilluser.user.dto.AnswerDto;
import com.skilluser.user.dto.StartTestRequest;
import com.skilluser.user.dto.StartTestResponse;
import com.skilluser.user.dto.ai.PsychometricAnalysisRequest;
import com.skilluser.user.dto.ai.PsychometricAnswerDto;
import com.skilluser.user.model.psychomatrictest.PsychometricTest;
import com.skilluser.user.service.PsychometricService;
import com.skilluser.user.utility.PaginationUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user/psychometric")
@CrossOrigin(origins = {"*"})

public class PsychometricController {

    private final PsychometricService psychometricService;

    @PostMapping
    public ResponseEntity<?> createTest(@RequestBody PsychometricTest psychometricTest) {
        return ResponseEntity.ok(psychometricService.createTest(psychometricTest));
    }

    @GetMapping
    public ResponseEntity<?> getTest(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size
            ,                        @RequestParam(defaultValue = "id") String sortBy,
                                     @RequestParam(defaultValue = "desc") String direction) {
        Pageable pageRequest = PaginationUtil.createPageRequest(page, size, sortBy, direction);

        return ResponseEntity.ok(psychometricService.getTests(pageRequest));
    }
    @PostMapping("/questions")
    public ResponseEntity<?> addMultipleQuestions(
            @RequestBody AddQuestionsRequest request) {

        psychometricService.addQuestions(request);
        return ResponseEntity.ok("Questions added successfully");
    }

    @PostMapping("/start")
    public StartTestResponse
    startTest(@RequestBody StartTestRequest req) {
        return psychometricService.startTest(req);
    }


    @PostMapping("/submit/{attemptId}/{userId}")
    public ResponseEntity<?> submitTest(
            @PathVariable Long attemptId,
            @PathVariable Long userId,
            @RequestBody List<AnswerDto> answers
    ) {
        Map<String, Object> result =
                psychometricService.submitTest(attemptId, userId, answers);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/summary/{userId}")
    public ResponseEntity<?> getSummary(@PathVariable Long userId) throws JsonProcessingException {
        return ResponseEntity.ok(psychometricService.getSummary(userId));
    }
}
