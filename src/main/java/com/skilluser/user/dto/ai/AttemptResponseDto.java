package com.skilluser.user.dto.ai;

import lombok.Data;

import java.util.List;

@Data
public class AttemptResponseDto {
    private Long attemptId;
    private String submittedAt;
    private List<UserAnswerResponseDto> responses;
}
