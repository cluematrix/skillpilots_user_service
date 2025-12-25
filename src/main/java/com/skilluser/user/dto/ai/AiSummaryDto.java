package com.skilluser.user.dto.ai;

import lombok.Data;

import java.util.List;

@Data
public class AiSummaryDto {

    private String summary;
    private List<String> strengths;
    private List<String> weaknesses;
    private List<String> suggestions;
}