package com.eventos.recuerdos.eventify_project.auth.controller;

import com.eventos.recuerdos.eventify_project.auth.domain.AuthenticationService;
import com.eventos.recuerdos.eventify_project.auth.dto.JwtAuthenticationResponse;
import com.eventos.recuerdos.eventify_project.auth.dto.SigninRequest;
import com.eventos.recuerdos.eventify_project.email.EmailService;
import com.eventos.recuerdos.eventify_project.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.Context;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    private EmailService emailService;

    @PostMapping("/signup")
    public ResponseEntity<JwtAuthenticationResponse> signup(@RequestBody User request) {
        return ResponseEntity.ok(authenticationService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody SigninRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @PostMapping("/bienvenido")
    public ResponseEntity<String> sendEmail(@RequestParam String email) {
        authenticationService.sendHelloEmail(email); // Publica el evento de correo
        return ResponseEntity.ok("¡Correo enviado a: " + email+"!");
    }
}


