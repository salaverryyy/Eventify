package com.eventos.recuerdos.eventify_project.auth.domain;

import com.eventos.recuerdos.eventify_project.HelloEmailEvent;
import com.eventos.recuerdos.eventify_project.auth.dto.JwtAuthenticationResponse;
import com.eventos.recuerdos.eventify_project.auth.dto.SigninRequest;
import com.eventos.recuerdos.eventify_project.auth.dto.UserSignupRequestDto;
import com.eventos.recuerdos.eventify_project.securityconfig.domain.JwtService;
import com.eventos.recuerdos.eventify_project.user.domain.UserAccount;
import com.eventos.recuerdos.eventify_project.user.domain.Role;
import com.eventos.recuerdos.eventify_project.user.infrastructure.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private UserAccountRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public JwtAuthenticationResponse signup(UserSignupRequestDto requestDto) {
        // Crear y configurar el nuevo UserAccount a partir del DTO
        UserAccount user = new UserAccount();
        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user.setUsername(requestDto.getUsername());
        user.setEmail(requestDto.getEmail());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setRole(Role.USER);  // Asignar el rol USER por defecto

        // Guardar el usuario en la base de datos
        userRepository.save(user);

        // Generar token JWT
        var jwt = jwtService.generateToken(user);
        JwtAuthenticationResponse response = new JwtAuthenticationResponse();
        response.setToken(jwt);

        // Publicar el evento para enviar el correo de bienvenida
        applicationEventPublisher.publishEvent(new HelloEmailEvent(user.getEmail()));

        return response;
    }

    public JwtAuthenticationResponse signin(SigninRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail());
        var jwt = jwtService.generateToken(user);

        JwtAuthenticationResponse response = new JwtAuthenticationResponse();
        response.setToken(jwt);

        return response;
    }
}
