package com.eventos.recuerdos.eventify_project.auth.domain;

import com.eventos.recuerdos.eventify_project.HelloEmailEvent;
import com.eventos.recuerdos.eventify_project.auth.dto.JwtAuthenticationResponse;
import com.eventos.recuerdos.eventify_project.auth.dto.SigninRequest;
import com.eventos.recuerdos.eventify_project.securityconfig.JwtService;
import com.eventos.recuerdos.eventify_project.user.domain.User;
import com.eventos.recuerdos.eventify_project.user.infrastructure.UserRepository;
import com.eventos.recuerdos.eventify_project.email.EmailService; // Asegúrate de importar EmailService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context; // Importar Context para Thymeleaf

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmailService emailService; // Inyección del EmailService

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public JwtAuthenticationResponse signup(User user) {
        // Codificamos la contraseña antes de guardar el usuario
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        // Generamos el token JWT para el usuario registrado
        var jwt = jwtService.generateToken(user);
        JwtAuthenticationResponse response = new JwtAuthenticationResponse();
        response.setToken(jwt);

        return response;
    }

    public void sendHelloEmail(String email) {
        applicationEventPublisher.publishEvent(new HelloEmailEvent(email));}

    public JwtAuthenticationResponse login(SigninRequest request) throws IllegalArgumentException {
        // Autenticación del usuario usando email y contraseña
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // Obtener el usuario de la base de datos
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + request.getEmail()));

        // Generar el token JWT
        var jwt = jwtService.generateToken(user);

        // Crear y devolver la respuesta con el token JWT
        JwtAuthenticationResponse response = new JwtAuthenticationResponse();
        response.setToken(jwt);

        return response;
    }
}
