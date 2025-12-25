package com.skilluser.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AiAnalysisRequest {

    private Long userId;
    private String username;
    private List<AiResponseDto> answers;
}