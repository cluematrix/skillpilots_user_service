package com.skilluser.user.dto.ai;

import lombok.Data;

@Data
public class AiAnswerPayload {

    private Long id;
    private AttemptPayload attempt;
    private QuestionPayload question;

    private String selectedAnswer;
    private String descriptiveAnswer;
}
