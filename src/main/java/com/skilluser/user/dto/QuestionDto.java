package com.skilluser.user.dto;

import com.skilluser.user.enums.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class QuestionDto {

    private Long questionId;
    private String questionText;
    private QuestionType type;
    private List<OptionDto> options;
}
