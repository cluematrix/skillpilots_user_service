package com.skilluser.user.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserAnswerResponseDto {
    private Long questionId;
    private String question;
    private String answer;
}
