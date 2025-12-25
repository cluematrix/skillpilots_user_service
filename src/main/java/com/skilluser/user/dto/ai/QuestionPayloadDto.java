package com.skilluser.user.dto.ai;

import lombok.Data;

@Data
public class QuestionPayloadDto {
    private String id;          // "Q1"
    private String section;
    private String type;
    private String questionText;
}