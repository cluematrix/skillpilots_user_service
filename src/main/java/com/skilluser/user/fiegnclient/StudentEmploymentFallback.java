package com.skilluser.user.fiegnclient;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class StudentEmploymentFallback implements StudentEmploymentClient {

    @Override
    public Integer getTotalExperience(Long studentId) {
        return 0; // default safe value
    }

    @Override
    public Map
            <String, Object> getWorkStatus(Long studentId) {
        Map<String, Object> fallback = new HashMap<>();
        fallback.put("status", "UNKNOWN");
        fallback.put("message", "Employment service unavailable");
        return fallback;
    }
}