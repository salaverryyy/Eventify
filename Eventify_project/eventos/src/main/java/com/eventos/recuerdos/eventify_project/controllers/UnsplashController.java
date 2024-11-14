// src/main/java/com/eventos/recuerdos/eventify/controller/UnsplashController.java
package com.eventos.recuerdos.eventify_project.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

@RestController
public class UnsplashController {

    @Value("${unsplash.access.key}")
    private String unsplashAccessKey;

    @GetMapping("/api/unsplash/random-image")
    public ResponseEntity<String> getRandomImage() {
        String url = "https://api.unsplash.com/photos/random?query=nature&orientation=landscape&client_id=" + unsplashAccessKey;
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForEntity(url, String.class);
    }
}




