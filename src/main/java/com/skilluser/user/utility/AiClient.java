package com.skilluser.user.utility;

import java.util.Map;

import com.skilluser.user.dto.AiAnalysisRequest;
import com.skilluser.user.dto.ai.AiAnalysisPayload;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class AiClient {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String AI_URL =
            "http://192.168.1.19:8000/api/v1/psychometric/analysis";




    public Object callAiAnalysis(AiAnalysisPayload payload) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AiAnalysisPayload> entity =
                new HttpEntity<>(payload, headers);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(AI_URL, entity, Map.class);

        Map<String, Object> body = response.getBody();

        if (body == null) {
            throw new RuntimeException("AI response body is null");
        }

        // 🔥 LOG RESPONSE FOR DEBUG (TEMP)
        System.out.println("AI RESPONSE = " + body);

        Object summaryObj = body.get("summary");

        if (summaryObj == null) {
            // fallback handling
            return body;   // return full AI response as string
        }

        return summaryObj;
    }

}
