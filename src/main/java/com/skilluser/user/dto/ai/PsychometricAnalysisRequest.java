package com.skilluser.user.dto.ai;

import lombok.Data;

import java.util.List;

@Data
public class PsychometricAnalysisRequest {

    private List<PsychometricAnswerDto> answers;

}
