package com.skilluser.user.serviceImpl;

import com.skilluser.user.fiegnclient.StudentEmploymentClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class StudentWorkStatusService {

    private final StudentEmploymentClient studentEmploymentClient;

    public StudentWorkStatusService(StudentEmploymentClient studentEmploymentClient) {
        this.studentEmploymentClient = studentEmploymentClient;
    }

    @CircuitBreaker(
            name = "STUDENT-SERVICE",
            fallbackMethod = "workStatusFallback"
    )
    public Map<String, Object> getWorkStatus(Long userId) {
        return studentEmploymentClient.getWorkStatus(userId);
    }

    public Map<String, Object> workStatusFallback(Long userId, Throwable ex) {
        return Map.of(
                "status", "UNKNOWN",
                "reason", "Student service unavailable"
        );
    }
}
