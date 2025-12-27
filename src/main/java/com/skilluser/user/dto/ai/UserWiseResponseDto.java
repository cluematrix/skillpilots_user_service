package com.skilluser.user.dto.ai;

import lombok.Data;

import java.util.List;

@Data
public class UserWiseResponseDto {
    private Long userId;
    private List<AttemptResponseDto> attempts;
}

