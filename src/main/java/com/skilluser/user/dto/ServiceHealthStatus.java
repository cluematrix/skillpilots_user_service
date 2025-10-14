package com.skilluser.user.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ServiceHealthStatus
{
    private String serviceName;
    private String status;
    private String url;
    private int port;
    private long responseTime;
    private String message;
    private LocalDateTime timestamp;
}
