package com.travelplanner.auth.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class AmadeusAuthService {

    @Value("${amadeus.client.id}")
    private String clientId;

    @Value("${amadeus.client.secret}")
    private String clientSecret;

    @Value("${amadeus.base.url}")
    private String baseUrl;

    private String accessToken;
    private long expiryTime;

    public String getAccessToken() {

        if (accessToken != null && System.currentTimeMillis() < expiryTime) {
            return accessToken;
        }

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);

        HttpEntity<?> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                baseUrl + "/v1/security/oauth2/token",
                request,
                Map.class
        );

        accessToken = (String) response.getBody().get("access_token");
        int expiresIn = (int) response.getBody().get("expires_in");
        expiryTime = System.currentTimeMillis() + (expiresIn * 1000);

        return accessToken;
    }
}

