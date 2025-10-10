package com.skilluser.user.controller;

import com.skilluser.user.dto.ServiceHealthStatus;
import com.skilluser.user.fiegnclient.CollegeServiceClient;
import com.skilluser.user.fiegnclient.CompanyServiceClient;
import com.skilluser.user.fiegnclient.StudentServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/api/monitor")
public class HealthMonitorController
{

    @Autowired
    private StudentServiceClient studentClient;

    @Autowired
    private CollegeServiceClient collegeClient;

    @Autowired
    private CompanyServiceClient companyClient;

    @Value("/api/v1/students")
    private String studentUrl;

    @Value("/api/v1/colleges")
    private String collegeUrl;

    @Value("/api/v1/companies")
    private String companyUrl;

    @Value("${server.port}")
    private int userServicePort;

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> checkAllServices()
    {
        Map<String, Object> allStatus = new HashMap<>();
        allStatus.put("timestamp", LocalDateTime.now());

        // Student Service
        allStatus.put("studentService", getServiceStatus("Student Service", studentUrl, studentClient));

        // College Service
        allStatus.put("collegeService", getServiceStatus("College Service", collegeUrl, collegeClient));

        // Company Service
        allStatus.put("companyService", getServiceStatus("Company Service", companyUrl, companyClient));

        long start = System.nanoTime();

        boolean isHealthy = true;
        String message = isHealthy ? "Running normally" : "Issues detected";

        try
        {
            Thread.sleep(1);
        }
        catch (InterruptedException ignored)
        {}

        long end = System.nanoTime();

        ServiceHealthStatus userService = new ServiceHealthStatus();
        userService.setServiceName("User Service");
        userService.setStatus(isHealthy ? "UP" : "DOWN");
        userService.setUrl("http://localhost:" + userServicePort + "/api/v1/users");
        userService.setPort(userServicePort);
        userService.setResponseTime((long) ((end - start) / 1_000_000.0));
        userService.setMessage(message);
        userService.setTimestamp(LocalDateTime.now());

        allStatus.put("userService", userService);


        return ResponseEntity.ok(allStatus);
    }

    private ServiceHealthStatus getServiceStatus(String serviceName, String url, Object client)
    {
        ServiceHealthStatus status = new ServiceHealthStatus();
        status.setServiceName(serviceName);
        status.setUrl(url);
        status.setTimestamp(LocalDateTime.now());

        long start = System.currentTimeMillis();
        try
        {
            if (client instanceof StudentServiceClient)
            {
                ((StudentServiceClient) client).checkHealth();
            }
            else if (client instanceof CollegeServiceClient)
            {
                ((CollegeServiceClient) client).checkHealth();
            }
            else if (client instanceof CompanyServiceClient)
            {
                ((CompanyServiceClient) client).checkHealth();
            }
            long end = System.currentTimeMillis();
            status.setStatus("UP");
            status.setResponseTime(end - start);
            status.setMessage("Service running normally");
        }
        catch (Exception e)
        {
            long end = System.currentTimeMillis();
            status.setStatus("DOWN");
            status.setResponseTime(end - start);
            status.setMessage(e.getMessage());
        }

        return status;
    }
}
