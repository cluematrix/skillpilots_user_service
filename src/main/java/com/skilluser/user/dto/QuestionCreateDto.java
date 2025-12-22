package com.skilluser.user.dto;

import com.skilluser.user.enums.QuestionType;
import com.skilluser.user.enums.TestSection;
import lombok.Data;

import java.util.List;

@Data
public class QuestionCreateDto {
    private TestSection section;
    private QuestionType type;
    private String questionText;

    // Only for MCQ
    private List<OptionDto> options;
}
