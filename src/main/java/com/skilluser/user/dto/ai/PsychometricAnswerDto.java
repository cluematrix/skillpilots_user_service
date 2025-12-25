package com.skilluser.user.dto.ai;

import lombok.Data;

@Data
public class PsychometricAnswerDto {

    private Long id;
    private AttemptDto attempt;
    private QuestionPayloadDto question;

    private String selectedAnswer;
    private String descriptiveAnswer;
}