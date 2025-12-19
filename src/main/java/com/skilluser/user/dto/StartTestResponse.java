package com.skilluser.user.dto;

import com.skilluser.user.enums.TestSection;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class StartTestResponse {

    private Long attemptId;
    private String testName;

    private Map<TestSection, List<QuestionDto>> sections;
}
