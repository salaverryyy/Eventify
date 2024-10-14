package com.eventos.recuerdos.eventify_project.auth.domain;

import com.eventos.recuerdos.eventify_project.Email.BienvenidaHtml;
import com.eventos.recuerdos.eventify_project.Email.MailManager;
import com.eventos.recuerdos.eventify_project.auth.dto.JwtAuthenticationResponse;
import com.eventos.recuerdos.eventify_project.auth.dto.SigninRequest;
import com.eventos.recuerdos.eventify_project.securityconfig.JwtService;
import com.eventos.recuerdos.eventify_project.user.domain.User;
import com.eventos.recuerdos.eventify_project.user.infrastructure.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthenticationService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    MailManager mailManager;

    public AuthenticationService(MailManager mailManager) {
        this.mailManager = mailManager;
    }

    public JwtAuthenticationResponse signup(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        JwtAuthenticationResponse response = new JwtAuthenticationResponse();
        response.setToken(jwt);
        // Enviamos el correo de bienvenida
        String emailContent = BienvenidaHtml.TEMPLATE_BIENVENIDA.replace("{{username}}", user.getUsername());
        mailManager.sendMessage(user.getEmail(), emailContent);
        return response;
    }

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
