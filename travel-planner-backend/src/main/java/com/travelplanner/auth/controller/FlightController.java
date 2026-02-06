package com.travelplanner.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.travelplanner.auth.service.AmadeusAuthService;
import com.travelplanner.auth.service.FlightSearchService;

@RestController
@RequestMapping("/api/flights")
@CrossOrigin(origins = "http://localhost:3000")
public class FlightController {

    @Autowired
    private FlightSearchService flightSearchService;

    @Autowired
    private AmadeusAuthService authService;

    @Value("${amadeus.base.url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * ---------------------------
     * 1️⃣ Flight Search API
     * ---------------------------
     */
    @GetMapping("/search")
    public Object searchFlights(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam String date
    ) {
        return flightSearchService.searchFlights(from, to, date);
    }

    /**
     * ----------------------------------------------------
     * 2️⃣ City / Airport Autocomplete API
     * Converts city name → IATA code
     * ----------------------------------------------------
     * Example:
     * /api/flights/locations?keyword=Bang
     */
    @GetMapping("/locations")
    public Object searchLocations(@RequestParam String keyword) {

        String accessToken = authService.getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = baseUrl
                + "/v1/reference-data/locations"
                + "?keyword=" + keyword
                + "&subType=CITY,AIRPORT";

        ResponseEntity<Object> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Object.class
        );

        return response.getBody();
    }
}
