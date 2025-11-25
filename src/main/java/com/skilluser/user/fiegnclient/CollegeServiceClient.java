package com.skilluser.user.fiegnclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@FeignClient(name="COLLEGE-SERVICE", path = "/api/v1/health")
public interface CollegeServiceClient
{
    @GetMapping("/college")
    Map<String, Object> checkHealth();

}
