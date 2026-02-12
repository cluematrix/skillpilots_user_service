package com.skilluser.user.fiegnclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name="STUDENT-SERVICE", path = "/api/v1/students/employment"

)
public interface StudentEmploymentClient
{
    @GetMapping("/experience/{studentId}")
    Integer getTotalExperience(@PathVariable("studentId") Long studentId);

    @GetMapping("/work-status/{studentId}")
    public Map<String, Object> getWorkStatus(@PathVariable Long studentId);

}
