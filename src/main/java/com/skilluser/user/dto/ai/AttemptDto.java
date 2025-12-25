package com.skilluser.user.dto.ai;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AttemptDto {
    private Long id;
    private Long userId;
    private boolean submitted;
    private LocalDateTime startedAt;
}