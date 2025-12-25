package com.skilluser.user.dto.ai;

import lombok.Data;

import java.util.List;

@Data
public class AiAnalysisPayload {
    private List<AiAnswerPayload> answers;
}
