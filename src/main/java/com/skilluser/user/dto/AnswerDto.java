package com.skilluser.user.dto;

import lombok.Data;

@Data
public class AnswerDto {

    private Long questionId;

    // for TRUE/FALSE / YES_NO / MCQ
    private String selectedAnswer;

    // for DESCRIPTIVE
    private String descriptiveAnswer;
}

