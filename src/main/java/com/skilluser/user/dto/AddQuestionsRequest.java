package com.skilluser.user.dto;

import lombok.Data;

import java.util.List;

@Data
public class AddQuestionsRequest {
    private Long testId;
    private List<QuestionCreateDto> questions;
}
