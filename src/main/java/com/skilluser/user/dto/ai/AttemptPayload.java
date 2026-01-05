package com.skilluser.user.dto.ai;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AttemptPayload {
    private Long id;
    private Long userId;
    private boolean submitted;
    private String startedAt;
}