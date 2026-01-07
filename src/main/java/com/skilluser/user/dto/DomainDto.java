package com.skilluser.user.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DomainDto {

    private Long mainDomainId;   // mapped from Domain.id
    private String mainDomainName;
    private LocalDateTime createdAt;
}
