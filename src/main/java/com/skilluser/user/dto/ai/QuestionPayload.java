package com.skilluser.user.dto.ai;

import lombok.Data;

@Data
public class QuestionPayload {
    private String id;
    private String section;
    private String type;
    private String questionText;
}