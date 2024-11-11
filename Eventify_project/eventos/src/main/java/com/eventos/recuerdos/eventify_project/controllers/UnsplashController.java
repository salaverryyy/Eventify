package com.eventos.recuerdos.eventify_project.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

@RestController
public class UnsplashController {

    @Value("${unsplash.access.key}")
    private String accessKey;

    private final String UNSPLASH_URL = "https://api.unsplash.com/photos/random?query=nature&orientation=landscape";

    @GetMapping("/api/unsplash")
    public ResponseEntity<String> getRandomImage() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Client-ID " + accessKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(UNSPLASH_URL, HttpMethod.GET, entity, String.class);

        return response;
    }
}
