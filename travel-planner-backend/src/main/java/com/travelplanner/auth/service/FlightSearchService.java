package com.travelplanner.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FlightSearchService {

    @Autowired
    private AmadeusAuthService authService;

    @Value("${amadeus.base.url}")
    private String baseUrl;

    public Object searchFlights(String origin, String destination, String date) {

        String token = authService.getAccessToken();

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        String url = baseUrl + "/v2/shopping/flight-offers"
                + "?originLocationCode=" + origin
                + "&destinationLocationCode=" + destination
                + "&departureDate=" + date
                + "&adults=1"
        		+ "&currencyCode=INR";

        ResponseEntity<Object> response =
                restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);

        return response.getBody();
    }
}
